package de.bundestag.android.synchronization;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.helpers.DateHelper;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.parser.objects.VisitorsArticleListItemGalleryObject;
import de.bundestag.android.parser.objects.VisitorsArticleListItemObject;
import de.bundestag.android.parser.objects.VisitorsArticleListObject;
import de.bundestag.android.parser.objects.VisitorsArticleObject;
import de.bundestag.android.parser.objects.VisitorsGalleryImageObject;
import de.bundestag.android.parser.objects.VisitorsGalleryObject;
import de.bundestag.android.parser.objects.VisitorsItemObject;
import de.bundestag.android.parser.objects.VisitorsObject;
import de.bundestag.android.storage.VisitorsDatabaseAdapter;

public class SynchronizeVisitorsTask extends BaseSynchronizeTask {
	private VisitorsSynchronization visitorsSynchronization;

	@Override
	protected Void doInBackground(Context... context) {
		this.activity = context[0];

		visitorsSynchronization = new VisitorsSynchronization();
		visitorsSynchronization.setup(context[0]);

		visitorsSynchronization.openDatabase();

		publishProgress("Bereich Besuch wird analysiert");

		VisitorsObject visitorsObject = null;
		try {
			visitorsObject = visitorsSynchronization.parseMainVisitors();
		} catch (Exception e) {
			BaseActivity baseActivity = (BaseActivity) activity;
			if (DataHolder.isOnline(baseActivity)) {
				publishProgress("Ein Problem ist beim Einfügen der Nachrichten in die Datenbank aufgetreten. Bitte versuchen Sie es später erneut.");
			} else {
				publishProgress("Die Aktualisierung der Daten kann leider nur mit einer aktiven Internetverbindung durchgeführt werden. Die Anwendung befindet sich im Offline-Modus.");

				this.cancel(false);
			}

			visitorsSynchronization.closeDatabase();

			return null;
		}

		publishProgress("Bilder aus Besuch werden geladen");

		if (visitorsObject != null) {
			// check if there are actually any changes
			boolean hasChanges = false;
			Cursor visitorsCursor = visitorsSynchronization.getAllVisitors();

			if ((visitorsCursor == null) || (visitorsCursor.getCount() == 0)) {
				hasChanges = true;
			} else {
				Map<String, VisitorsItemObject> databaseVisitors = getVisitorObjectsFromCursor(visitorsCursor);
				// List<VisitorsItemObject> databaseVisitors =
				// getVisitorsFromCursor(visitorsCursor);
				visitorsCursor.close();

				List<VisitorsItemObject> parsedVisitors = visitorsObject.getItems();

				VisitorsItemObject parsedVisitor;
				for (int i = 0; i < parsedVisitors.size(); i++) {
					parsedVisitor = parsedVisitors.get(i);
					// if doesn't exist, then hasChanges = true and break
					// if (!existsVisitor(databaseVisitors,
					// parsedVisitor.getId()))
					if (!databaseVisitors.containsKey(parsedVisitor.getId())) {
						hasChanges = true;
						break;
					}
					// if exists, but date from parsed after, then hasChanges =
					// true and break
					else {
						VisitorsItemObject databaseVisitor = databaseVisitors.get(parsedVisitor.getId());
						// VisitorsItemObject databaseVisitor =
						// getDatabaseVisitor(databaseVisitors,
						// parsedVisitor.getId());
						Date databaseVisitorDate = DateHelper.parseArticleDate(databaseVisitor.getDate());
						Date parsedVisitorDate = DateHelper.parseArticleDate(parsedVisitor.getDate());

						if (parsedVisitorDate.after(databaseVisitorDate)) {
							hasChanges = true;
							break;
						}
					}
				}
			}

			if (hasChanges) {
				try {
					insertPictures(visitorsObject);

					visitorsSynchronization.deleteAllVisitors();

					storeVisitorItems(visitorsObject.getItems());
					storeLocations(visitorsObject.getLocations());
					storeOffers(visitorsObject.getOffers());
					storeArticleLists(visitorsObject.getArticleLists());
					storeContact(visitorsObject.getContact());
					storeGalleries(visitorsObject.getGalleries());

					storeArticleDetails(visitorsObject.getNewsArticles());
				} catch (Exception e) {
					BaseActivity baseActivity = (BaseActivity) activity;
					if (DataHolder.isOnline(baseActivity)) {
						publishProgress("Ein Problem ist beim Einfügen der Nachrichten in die Datenbank aufgetreten. Bitte versuchen Sie es später erneut.");
					} else {
						publishProgress("Die Aktualisierung der Daten kann leider nur mit einer aktiven Internetverbindung durchgeführt werden. Die Anwendung befindet sich im Offline-Modus.");

						this.cancel(false);
					}
				}
			}
		}

		visitorsSynchronization.closeDatabase();

		progressDialog.dismiss();

		return null;
	}

	// private VisitorsItemObject getDatabaseVisitor(List<VisitorsItemObject>
	// databaseVisitors, String visitorId)
	// {
	// for (int j = 0; j < databaseVisitors.size(); j++)
	// {
	// if (databaseVisitors.get(j).getId().equals(visitorId))
	// {
	// return databaseVisitors.get(j);
	// }
	// }
	//
	// return null;
	// }

	// private boolean existsVisitor(List<VisitorsItemObject> databaseVisitors,
	// String visitorId)
	// {
	// for (int j = 0; j < databaseVisitors.size(); j++)
	// {
	// if (databaseVisitors.get(j).getId().equals(visitorId))
	// {
	// return true;
	// }
	// }
	//
	// return false;
	// }

	/**
	 * Insert the main visitor objects. This is used for updating.
	 */
	private void storeVisitorItems(List<VisitorsItemObject> items) {
		for (int i = 0; i < items.size(); i++) {
			visitorsSynchronization.insertAVisitor(items.get(i));
		}
	}

	/**
	 * Get the current visitor objects from the database.
	 */
	// private List<VisitorsItemObject> getVisitorsFromCursor(Cursor
	// visitorsCursor)
	// {
	// List<VisitorsItemObject> visitorObjects = new
	// ArrayList<VisitorsItemObject>();
	// VisitorsItemObject visitorObject;
	//
	// while (!visitorsCursor.isAfterLast())
	// {
	// visitorObject = new VisitorsItemObject();
	//
	// visitorObject.setId(visitorsCursor.getString(visitorsCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ID)));
	// visitorObject.setDate(visitorsCursor.getString(visitorsCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_DATE)));
	//
	// visitorObjects.add(visitorObject);
	//
	// visitorsCursor.moveToNext();
	// }
	//
	// return visitorObjects;
	// }

	/**
	 * Get the current visitor objects from the database.
	 */
	private HashMap<String, VisitorsItemObject> getVisitorObjectsFromCursor(Cursor visitorsCursor) {
		List<VisitorsItemObject> visitorObjects = new ArrayList<VisitorsItemObject>();
		VisitorsItemObject visitorObject;
		while (!visitorsCursor.isAfterLast()) {
			visitorObject = new VisitorsItemObject();

			visitorObject.setId(visitorsCursor.getString(visitorsCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ID)));
			visitorObject.setDate(visitorsCursor.getString(visitorsCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_DATE)));

			visitorObjects.add(visitorObject);

			visitorsCursor.moveToNext();
		}

		HashMap<String, VisitorsItemObject> visitorsMap = new HashMap<String, VisitorsItemObject>();
		for (int i = 0; i < visitorObjects.size(); i++) {
			visitorsMap.put(visitorObjects.get(i).getId(), visitorObjects.get(i));
		}

		return visitorsMap;
	}

	public void insertPictures(VisitorsObject visitorsObject) {
		Map<String, String> imageMap = new HashMap<String, String>();
		List<VisitorsItemObject> images = visitorsObject.getImages();
		for (int i = 0; i < images.size(); i++) {
			publishProgress("Bilder aus Besuch werden geladen (" + (i + 1) + "/" + images.size() + ")");

			VisitorsItemObject visitorsItemObject = (VisitorsItemObject) images.get(i);
			Bitmap bitmap = ImageHelper.loadBitmapFromUrl(visitorsItemObject.getUrl());
			if (bitmap != null) {
				String imageString = ImageHelper.convertBitmapToString(bitmap);
				visitorsItemObject.setImageString(imageString);

				imageMap.put(visitorsItemObject.getId(), imageString);
			}
		}

		if ((imageMap != null) && (imageMap.size() > 0)) {
			String imageString;

			List<VisitorsItemObject> items = visitorsObject.getItems();
			if (items != null) {
				for (int i = 0; i < items.size(); i++) {
					VisitorsItemObject item = items.get(i);
					if ((item.getType().equals("image")) && (item.getId() != null)) {
						imageString = imageMap.get(item.getId());
						item.setImageString(imageString);
					}
				}
			}

			// galleries
			List<VisitorsGalleryObject> galleries = visitorsObject.getGalleries();
			if (galleries != null) {
				for (int i = 0; i < galleries.size(); i++) {
					VisitorsGalleryObject gallery = galleries.get(i);

					if (gallery != null) {
						List<VisitorsGalleryImageObject> galleriesImages = gallery.getImages();

						if (galleriesImages != null) {
							for (int j = 0; j < galleriesImages.size(); j++) {
								VisitorsGalleryImageObject galImage = galleriesImages.get(j);

								if (galImage != null) {
									imageString = imageMap.get(galImage.getImageId());
									galImage.setImageString(imageString);
								}
							}
						}
					}
				}
			}

			// sub lists
			List<VisitorsArticleListObject> articleLists = visitorsObject.getArticleLists();
			if (articleLists != null) {
				for (int k = 0; k < articleLists.size(); k++) {
					VisitorsArticleListObject visitorsArticleList = articleLists.get(k);

					if (visitorsArticleList != null) {
						List<VisitorsArticleListItemObject> articleItems = visitorsArticleList.getItems();
						if (articleItems != null) {
							for (int i = 0; i < articleItems.size(); i++) {
								VisitorsArticleListItemObject article = articleItems.get(i);

								if (article.getImageId() != null) {
									imageString = imageMap.get(article.getImageId());
									article.setImageString(imageString);
								}

								List<VisitorsArticleListItemGalleryObject> galleryImages = article.getGalleryImages();
								if (galleryImages != null) {
									for (int j = 0; j < galleryImages.size(); j++) {
										VisitorsArticleListItemGalleryObject galleryImage = galleryImages.get(j);

										if (galleryImage.getImageId() != null) {
											imageString = imageMap.get(galleryImage.getImageId());
											galleryImage.setImageString(imageString);
										}
									}
								}
							}
						}
					}
				}
			}

			// article details
			List<VisitorsArticleObject> articles = visitorsObject.getNewsArticles();
			if (articles != null) {
				for (int i = 0; i < articles.size(); i++) {
					VisitorsArticleObject article = articles.get(i);

					if (article.getImageId() != null) {
						imageString = imageMap.get(article.getImageId());
						article.setImageString(imageString);
					}
				}
			}

			// offers
			VisitorsArticleListObject offers = visitorsObject.getOffers();
			if (offers != null) {
				List<VisitorsArticleListItemObject> offersItems = offers.getItems();
				if (offersItems != null) {
					for (int i = 0; i < offersItems.size(); i++) {
						VisitorsArticleListItemObject article = offersItems.get(i);

						if (article.getImageId() != null) {
							imageString = imageMap.get(article.getImageId());
							article.setImageString(imageString);
						}

						List<VisitorsArticleListItemGalleryObject> galleryImages = article.getGalleryImages();
						if (galleryImages != null) {
							for (int j = 0; j < galleryImages.size(); j++) {
								VisitorsArticleListItemGalleryObject galleryImage = galleryImages.get(j);

								if (galleryImage.getImageId() != null) {
									imageString = imageMap.get(galleryImage.getImageId());
									galleryImage.setImageString(imageString);
								}
							}
						}
					}
				}
			}

			// locations
			VisitorsArticleListObject locations = visitorsObject.getLocations();
			if (locations != null) {
				List<VisitorsArticleListItemObject> locationsItems = locations.getItems();
				if (locationsItems != null) {
					for (int i = 0; i < locationsItems.size(); i++) {
						VisitorsArticleListItemObject article = locationsItems.get(i);

						if (article.getImageId() != null) {
							imageString = imageMap.get(article.getImageId());
							article.setImageString(imageString);
						}

						List<VisitorsArticleListItemGalleryObject> galleryImages = article.getGalleryImages();
						if (galleryImages != null) {
							for (int j = 0; j < galleryImages.size(); j++) {
								VisitorsArticleListItemGalleryObject galleryImage = galleryImages.get(j);

								if (galleryImage.getImageId() != null) {
									imageString = imageMap.get(galleryImage.getImageId());
									galleryImage.setImageString(imageString);
								}
							}
						}
					}
				}
			}
		}
	}

	private void storeArticleLists(List<VisitorsArticleListObject> articleLists) {
		publishProgress("Artikel für Angebote und Orte werden geladen");

		if (articleLists != null) {
			for (int i = 0; i < articleLists.size(); i++) {
				VisitorsArticleListObject visitorsArticleList = articleLists.get(i);

				long articleListId = visitorsSynchronization.insertVisitorArticleList(visitorsArticleList, VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_ARTICLELIST);

				List<VisitorsArticleListItemObject> articles = visitorsArticleList.getItems();
				if (articles != null) {
					for (int j = 0; j < articles.size(); j++) {
						VisitorsArticleListItemObject listArticle = articles.get(j);

						visitorsSynchronization.insertVisitorArticleListArticle(listArticle, VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_NORMAL, articleListId);

						if (progressDialog.cancelSynchronization) {
							break;
						}
					}
				}

				if (progressDialog.cancelSynchronization) {
					break;
				}
			}
		}
	}

	private void storeArticleDetails(List<VisitorsArticleObject> articles) {
		publishProgress("Artikelinformationen werden gespeichert");

		if (articles != null) {
			for (int i = 0; i < articles.size(); i++) {
				VisitorsArticleObject locationArticle = articles.get(i);

				visitorsSynchronization.insertVisitorArticle(locationArticle, VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_NORMAL);

				if (progressDialog.cancelSynchronization) {
					break;
				}
			}
		}
	}

	private void storeContact(VisitorsArticleObject contact) {
		visitorsSynchronization.insertVisitorArticle(contact, VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_CONTACT);
	}

	private void storeGalleries(List<VisitorsGalleryObject> galleries) {
		publishProgress("Galerien werden gespeichert");

		for (int i = 0; i < galleries.size(); i++) {
			VisitorsGalleryObject gallery = galleries.get(i);

			if (gallery != null) {
				long galleryId = visitorsSynchronization.insertGallery(gallery);

				List<VisitorsGalleryImageObject> galleryImages = gallery.getImages();
				if (galleryImages != null) {
					for (int j = 0; j < galleryImages.size(); j++) {
						VisitorsGalleryImageObject galleryImage = galleryImages.get(j);
						if (galleryImage != null) {
							visitorsSynchronization.insertVisitorGalleryImage(galleryImage, galleryId);
						}
					}
				}
			}

			if (progressDialog.cancelSynchronization) {
				break;
			}
		}
	}

	private void storeLocations(VisitorsArticleListObject locationsObject) {
		publishProgress("Orte werden gespeichert");

		if (locationsObject != null) {
			long articleListId;
			Cursor offersArticleListCursor = visitorsSynchronization.getVisitorArticleList(VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_LOCATIONS);
			if ((offersArticleListCursor != null) && (offersArticleListCursor.getCount() > 0)) {
				articleListId = offersArticleListCursor.getInt(offersArticleListCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLELIST_ROWID));
				// articleListId =
				// offersArticleListCursor.getInt(offersArticleListCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_ID));

			} else {
				articleListId = visitorsSynchronization.insertVisitorArticleList(locationsObject, VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_LOCATIONS);
				// visitorsSynchronization.insertVisitorArticleList(locationsObject,
				// VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_LOCATIONS);

			}
			offersArticleListCursor.close();

			List<VisitorsArticleListItemObject> locationsArticles = locationsObject.getItems();
			if (locationsArticles != null) {
				for (int i = 0; i < locationsArticles.size(); i++) {
					publishProgress("Orte werden auf vorhandene Aktualisierungen überprüft " + (i + 1) + "/" + locationsArticles.size() + ".");

					VisitorsArticleListItemObject locationArticle = locationsArticles.get(i);

					Cursor locationsArticleCursor = visitorsSynchronization.getVisitorArticle(locationArticle.getId());
					if ((locationsArticleCursor == null) || (locationsArticleCursor.getCount() == 0)) {
						publishProgress("Orte werden auf vorhandene Aktualisierungen überprüft " + (i + 1) + "/" + locationsArticles.size() + ".");

						visitorsSynchronization.insertVisitorArticleListArticle(locationArticle, VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_LOCATIONS, articleListId);

						List<VisitorsArticleListItemGalleryObject> articleGalleryImages = locationArticle.getGalleryImages();
						if (articleGalleryImages != null) {
							for (int j = 0; j < articleGalleryImages.size(); j++) {
								VisitorsArticleListItemGalleryObject articleGalleryImage = articleGalleryImages.get(j);

								// TODO - OPTIMIZATION run a check with the main
								// table, and get the image if it exists

								// TODO - load picture and insert in object

								visitorsSynchronization.insertVisitorArticleGalleryImage(articleGalleryImage, articleListId);
							}
						}
					}
					locationsArticleCursor.close();

					if (progressDialog.cancelSynchronization) {
						break;
					}
				}
			}
		}
	}

	private void storeOffers(VisitorsArticleListObject offers) {
		publishProgress("Angebote werden gespeichert");

		if (offers != null) {
			long articleListId;
			Cursor offersArticleListCursor = visitorsSynchronization.getVisitorArticleList(VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_OFFERS);
			if ((offersArticleListCursor != null) && (offersArticleListCursor.getCount() > 0)) {
				articleListId = offersArticleListCursor.getInt(offersArticleListCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLELIST_ROWID));
			} else {
				articleListId = visitorsSynchronization.insertVisitorArticleList(offers, VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_OFFERS);
			}
			offersArticleListCursor.close();

			List<VisitorsArticleListItemObject> offersArticles = offers.getItems();
			if (offersArticles != null) {
				for (int i = 0; i < offersArticles.size(); i++) {
					publishProgress("Angebote werden auf vorhandene Aktualisierungen überprüft " + (i + 1) + "/" + offersArticles.size() + ".");

					VisitorsArticleListItemObject offerArticle = offersArticles.get(i);

					Cursor offersArticleCursor = visitorsSynchronization.getVisitorArticle(offerArticle.getId());
					if ((offersArticleCursor == null) || (offersArticleCursor.getCount() == 0)) {
						publishProgress("Angebote werden überprüft " + (i + 1) + "/" + offersArticles.size() + ".");

						visitorsSynchronization.insertVisitorArticleListArticle(offerArticle, VisitorsSynchronization.ARTICLE_SPECIFIC_TYPE_OFFERS, articleListId);

						List<VisitorsArticleListItemGalleryObject> articleGalleryImages = offerArticle.getGalleryImages();
						if (articleGalleryImages != null) {
							for (int j = 0; j < articleGalleryImages.size(); j++) {
								VisitorsArticleListItemGalleryObject articleGalleryImage = articleGalleryImages.get(j);

								// TODO - load picture and insert in object

								visitorsSynchronization.insertVisitorArticleGalleryImage(articleGalleryImage, articleListId);
							}
						}
					}
					offersArticleCursor.close();

					if (progressDialog.cancelSynchronization) {
						break;
					}
				}
			}
		}
	}
}
