package de.bundestag.android.sections.plenum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.helpers.DateHelper;
import de.bundestag.android.parser.PlenumXMLParser;
import de.bundestag.android.parser.objects.PlenumObject;
import de.bundestag.android.parser.objects.PlenumSimpleObject;
import de.bundestag.android.sections.news.NewsActivity;
import de.bundestag.android.sections.news.NewsActivityTablet;
import de.bundestag.android.storage.PlenumDatabaseAdapter;
import de.bundestag.android.synchronization.PlenumSynchronization;

public class PlenumSitzungenActivity extends BaseActivity {
	private ArrayList<String> positionTypes = new ArrayList<String>();
	// public Activity activity = null;
	boolean loadTxt = false;
	boolean offlineClicked = false;
	int id = -1;
	PlenumObject mainPlenum;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);
//		if(AppConstant.isFragmentSupported){
//			DataHolder.dismissProgress();
//			}
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				DataHolder.mLockScreenRotation(PlenumSitzungenActivity.this);
				SharedPreferences settings = getSharedPreferences("Identify", 0);
				SharedPreferences.Editor editor = settings.edit();
				if (settings.getInt("LeftFragmentSize", -1) != -1) {
					AppConstant.isFragmentSupported = settings.getBoolean("FragmentSupport", false);
					DataHolder.calculatedScreenResolution = settings.getInt("LeftFragmentSize", -1);
				} else {
					editor.putBoolean("FragmentSupport", AppConstant.isFragmentSupported);
					editor.putInt("LeftFragmentSize", DataHolder.calculatedScreenResolution);
					editor.commit();
				}
				try{
				DataHolder.createProgressDialog(PlenumSitzungenActivity.this);
				}catch (Exception e) {
				}

			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				try{
				setContentView(R.layout.plenum_sitzungen);
				
				if (AppConstant.isFragmentSupported) {
					((LinearLayout) findViewById(R.id.lnrScrollable)).setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution,
							LinearLayout.LayoutParams.FILL_PARENT));
					((TextView) findViewById(R.id.mainTitle)).setLayoutParams(new LinearLayout.LayoutParams(DataHolder.calculatedScreenResolution,
							LinearLayout.LayoutParams.WRAP_CONTENT));
				}
				if (DataHolder.isOnline(PlenumSitzungenActivity.this) || !AppConstant.isFragmentSupported) {
					PlenumDatabaseAdapter pdb = new PlenumDatabaseAdapter(PlenumSitzungenActivity.this);
					pdb.open();
					Cursor mainSit = pdb.fetchPlenumByType(PlenumSynchronization.PLENUM_TYPE_MAIN);
					if ((mainSit != null) && (mainSit.getCount() > 0)) {
						mainSit.moveToFirst();
						String teaser = mainSit.getString(mainSit.getColumnIndex(PlenumDatabaseAdapter.KEY_TEASER));

						// ArrayList<Date> sitDates = new ArrayList<Date>();
						// ArrayList<String> sitTitles = new
						// ArrayList<String>();

						long[] sitTimes = new long[4];
						HashMap<String, Cursor> sits = new HashMap<String, Cursor>();

						if (teaser.indexOf(PlenumXMLParser.SIMPLE1_PLENUM_URL) > 0) {
							Cursor sitCursor = pdb.fetchPlenumByType(PlenumSynchronization.PLENUM_TYPE_SIMPLE1);
							if (sitCursor.getCount() > 0) {
								sitCursor.moveToFirst();
								String sitCursor1DateStr = sitCursor.getString(sitCursor.getColumnIndex(PlenumDatabaseAdapter.KEY_DATE));
								if (sitCursor1DateStr != null && !sitCursor1DateStr.equals("")) {
									long sitCursorTime = DateHelper.parseDate(sitCursor1DateStr).getTime();
									sitTimes[0] = sitCursorTime;
									sits.put(sitCursorTime + "", sitCursor);
								}
							}
						}
						if (teaser.indexOf(PlenumXMLParser.SIMPLE2_PLENUM_URL) > 0) {
							Cursor sitCursor = pdb.fetchPlenumByType(PlenumSynchronization.PLENUM_TYPE_SIMPLE2);
							if (sitCursor.getCount() > 0) {
								sitCursor.moveToFirst();
								String sitCursor1DateStr = sitCursor.getString(sitCursor.getColumnIndex(PlenumDatabaseAdapter.KEY_DATE));
								if (sitCursor1DateStr != null && !sitCursor1DateStr.equals("")) {
									long sitCursorTime = DateHelper.parseDate(sitCursor1DateStr).getTime();
									sitTimes[1] = sitCursorTime;
									sits.put(sitCursorTime + "", sitCursor);
								}
							}
						}
						if (teaser.indexOf(PlenumXMLParser.SIMPLE3_PLENUM_URL) > 0) {
							Cursor sitCursor = pdb.fetchPlenumByType(PlenumSynchronization.PLENUM_TYPE_SIMPLE3);
							if (sitCursor.getCount() > 0) {
								sitCursor.moveToFirst();
								String sitCursor1DateStr = sitCursor.getString(sitCursor.getColumnIndex(PlenumDatabaseAdapter.KEY_DATE));
								if (sitCursor1DateStr != null && !sitCursor1DateStr.equals("")) {
									long sitCursorTime = DateHelper.parseDate(sitCursor1DateStr).getTime();
									sitTimes[2] = sitCursorTime;
									sits.put(sitCursorTime + "", sitCursor);
								}
							}
						}
						if (teaser.indexOf(PlenumXMLParser.SIMPLE4_PLENUM_URL) > 0) {
							Cursor sitCursor = pdb.fetchPlenumByType(PlenumSynchronization.PLENUM_TYPE_SIMPLE4);
							if (sitCursor.getCount() > 0) {
								sitCursor.moveToFirst();
								String sitCursor1DateStr = sitCursor.getString(sitCursor.getColumnIndex(PlenumDatabaseAdapter.KEY_DATE));
								if (sitCursor1DateStr != null && !sitCursor1DateStr.equals("")) {
									long sitCursorTime = DateHelper.parseDate(sitCursor1DateStr).getTime();
									sitTimes[3] = sitCursorTime;
									sits.put(sitCursorTime + "", sitCursor);
								}
							}
						}

						Arrays.sort(sitTimes);
						int position = 1;
						for (long sitTime : sitTimes) {
							if (sitTime != 0) {
								Cursor sitCursor = sits.get(sitTime + "");
								positionTypes.add(sitCursor.getString(sitCursor.getColumnIndex(PlenumDatabaseAdapter.KEY_TYPE)));
								String txt = sitCursor.getString(sitCursor.getColumnIndex(PlenumDatabaseAdapter.KEY_TITLE));
								String txtArr[] = txt.split(",");
								switch (position) {
								case 1: {
									if (AppConstant.isFragmentSupported) {
										if (txtArr.length == 4) {
											((TextView) findViewById(R.id.sitzungen_line1_text1)).setText(txtArr[0].trim());
											((TextView) findViewById(R.id.sitzungen_line1_text2)).setText((txtArr[1] + ", " + txtArr[2]).trim());
											((TextView) findViewById(R.id.sitzungen_line1_text3)).setText(txtArr[3].trim());
										} else {
											txt = txt.replace(", ", "\n");
											((TextView) findViewById(R.id.sitzungen_line1_text1)).setText(txt);
										}
									} else {
										((TextView) findViewById(R.id.sitzungen_line1_text)).setText(txt);
									}
									findViewById(R.id.sitzungen_line1).setVisibility(View.VISIBLE);
									if (!loadTxt && AppConstant.isFragmentSupported) {
										PlenumObjectViewFragment plenumObjectViewFragment = (PlenumObjectViewFragment) PlenumSitzungenActivity.this.getSupportFragmentManager()
												.findFragmentById(R.id.detailsFragment);
										// plenumObjectViewFragment.setPlenumObjectType(positionTypes.get(0));
										// plenumObjectViewFragment.createPlenumObject();
										// loadTxt = true;
										id = 0;
									}
									break;
								}
								case 2: {
									if (AppConstant.isFragmentSupported) {
										if (txtArr.length == 4) {
											((TextView) findViewById(R.id.sitzungen_line2_text1)).setText(txtArr[0].trim());
											((TextView) findViewById(R.id.sitzungen_line2_text2)).setText((txtArr[1] + ", " + txtArr[2]).trim());
											((TextView) findViewById(R.id.sitzungen_line2_text3)).setText(txtArr[3].trim());
										} else {
											txt = txt.replace(", ", "\n");
											((TextView) findViewById(R.id.sitzungen_line2_text1)).setText(txt);
										}
									} else {
										((TextView) findViewById(R.id.sitzungen_line2_text)).setText(txt);
									}
									findViewById(R.id.sitzungen_line2).setVisibility(View.VISIBLE);
									if (!loadTxt && AppConstant.isFragmentSupported) {
										PlenumObjectViewFragment plenumObjectViewFragment = (PlenumObjectViewFragment) PlenumSitzungenActivity.this.getSupportFragmentManager()
												.findFragmentById(R.id.detailsFragment);
										// plenumObjectViewFragment.setPlenumObjectType(positionTypes.get(1));
										// plenumObjectViewFragment.createPlenumObject();
										// loadTxt = true;
										id = 1;
									}
									break;
								}
								case 3: {
									if (AppConstant.isFragmentSupported) {
										if (txtArr.length == 4) {
											((TextView) findViewById(R.id.sitzungen_line3_text1)).setText(txtArr[0].trim());
											((TextView) findViewById(R.id.sitzungen_line3_text2)).setText((txtArr[1] + ", " + txtArr[2]).trim());
											((TextView) findViewById(R.id.sitzungen_line3_text3)).setText(txtArr[3].trim());
										} else {
											txt = txt.replace(", ", "\n");
											((TextView) findViewById(R.id.sitzungen_line3_text1)).setText(txt);
										}
									} else {
										((TextView) findViewById(R.id.sitzungen_line3_text)).setText(txt);
									}

									findViewById(R.id.sitzungen_line3).setVisibility(View.VISIBLE);
									if (!loadTxt && AppConstant.isFragmentSupported) {
										PlenumObjectViewFragment plenumObjectViewFragment = (PlenumObjectViewFragment) PlenumSitzungenActivity.this.getSupportFragmentManager()
												.findFragmentById(R.id.detailsFragment);
										// plenumObjectViewFragment.setPlenumObjectType(positionTypes.get(2));
										// plenumObjectViewFragment.createPlenumObject();
										// loadTxt = true;
										id = 2;
									}
									break;
								}
								case 4: {
									if (AppConstant.isFragmentSupported) {
										if (txtArr.length == 4) {
											((TextView) findViewById(R.id.sitzungen_line4_text1)).setText(txtArr[0].trim());
											((TextView) findViewById(R.id.sitzungen_line4_text2)).setText((txtArr[1] + ", " + txtArr[2]).trim());
											((TextView) findViewById(R.id.sitzungen_line4_text3)).setText(txtArr[3].trim());
										} else {
											txt = txt.replace(", ", "\n");
											((TextView) findViewById(R.id.sitzungen_line4_text1)).setText(txt);
										}
									} else {
										((TextView) findViewById(R.id.sitzungen_line4_text)).setText(txt);
									}
									findViewById(R.id.sitzungen_line4).setVisibility(View.VISIBLE);
									if (!loadTxt && AppConstant.isFragmentSupported) {
										PlenumObjectViewFragment plenumObjectViewFragment = (PlenumObjectViewFragment) PlenumSitzungenActivity.this.getSupportFragmentManager()
												.findFragmentById(R.id.detailsFragment);
										id = 3;
									}
									break;
								}
								}
								position++;
							}
						}
						Iterator it = sits.entrySet().iterator();
						while (it.hasNext()) {
							Map.Entry pairs = (Map.Entry) it.next();
							Cursor sit = (Cursor) pairs.getValue();
							sit.close();
							it.remove();
						}

						for (int i = 0; i < sits.size(); i++) {
							Cursor sit = sits.get(i);
							if (sit != null) {
								sit.close();
							}
						}
					}
					mainSit.close();
					pdb.close();
				}
//				else {
//					((TextView) findViewById(R.id.secondTitle)).setVisibility(View.GONE);
//				}
				if (AppConstant.isFragmentSupported) {
					PlenumObjectViewFragment plenumObjectViewFragment = (PlenumObjectViewFragment) PlenumSitzungenActivity.this.getSupportFragmentManager().findFragmentById(
							R.id.detailsFragment);
					plenumObjectViewFragment.getPlenumTaskObject();
				}
				}catch(Exception e){
					
				}
				try{
				DataHolder.dismissProgress();
				DataHolder.releaseScreenLock(PlenumSitzungenActivity.this);
				}catch (Exception e) {
				}
			}

			@Override
			protected Void doInBackground(Void... params) {
				PlenumSynchronization plenumSynchronization = new PlenumSynchronization();
				plenumSynchronization.setup(PlenumSitzungenActivity.this);
				plenumSynchronization.openDatabase();
				try {
					mainPlenum = plenumSynchronization.parsePlenum(PlenumXMLParser.MAIN_PLENUM_URL);
					mainPlenum.setType(PlenumSynchronization.PLENUM_TYPE_MAIN);
					plenumSynchronization.deleteAllPlenum();
					plenumSynchronization.insertPicture(mainPlenum);
					plenumSynchronization.insertAPlenum(mainPlenum);

					PlenumObject taskPlenum = plenumSynchronization.parsePlenum(PlenumXMLParser.PLENUM_TASK_URL);
					taskPlenum.setType(PlenumSynchronization.PLENUM_TYPE_TASK);
					plenumSynchronization.insertPicture(taskPlenum);
					plenumSynchronization.insertAPlenum(taskPlenum);
				} catch (Exception e) {

				}

				if (mainPlenum != null && mainPlenum.getTeaser() != null && mainPlenum.getTeaser().indexOf(PlenumXMLParser.SIMPLE1_PLENUM_URL) > 0) {
					try {
						PlenumSimpleObject simplePlenum1 = plenumSynchronization.parseSimplePlenum(PlenumXMLParser.SIMPLE1_PLENUM_URL);
						simplePlenum1.setType(PlenumSynchronization.PLENUM_TYPE_SIMPLE1);
						plenumSynchronization.insertASimplePlenum(simplePlenum1);
					} catch (Exception e) {
						// publishProgress("Ein Problem ist beim Analysieren der Plenum aufgetreten. Bitte versuchen Sie es später erneut.");
					}
				}

				if (mainPlenum != null && mainPlenum.getTeaser() != null && mainPlenum.getTeaser().indexOf(PlenumXMLParser.SIMPLE2_PLENUM_URL) > 0) {
					try {
						PlenumSimpleObject simplePlenum2 = plenumSynchronization.parseSimplePlenum(PlenumXMLParser.SIMPLE2_PLENUM_URL);
						simplePlenum2.setType(PlenumSynchronization.PLENUM_TYPE_SIMPLE2);
						plenumSynchronization.insertASimplePlenum(simplePlenum2);
					} catch (Exception e) {
						// publishProgress("Ein Problem ist beim Analysieren der Plenum aufgetreten. Bitte versuchen Sie es später erneut.");
					}
				}

				if (mainPlenum != null && mainPlenum.getTeaser() != null && mainPlenum.getTeaser().indexOf(PlenumXMLParser.SIMPLE3_PLENUM_URL) > 0) {
					try {
						PlenumSimpleObject simplePlenum3 = plenumSynchronization.parseSimplePlenum(PlenumXMLParser.SIMPLE3_PLENUM_URL);
						simplePlenum3.setType(PlenumSynchronization.PLENUM_TYPE_SIMPLE3);
						plenumSynchronization.insertASimplePlenum(simplePlenum3);
					} catch (Exception e) {
						// publishProgress("Ein Problem ist beim Analysieren der Plenum aufgetreten. Bitte versuchen Sie es später erneut.");
					}
				}

				if (mainPlenum != null && mainPlenum.getTeaser() != null && mainPlenum.getTeaser().indexOf(PlenumXMLParser.SIMPLE4_PLENUM_URL) > 0) {
					try {
						PlenumSimpleObject simplePlenum4 = plenumSynchronization.parseSimplePlenum(PlenumXMLParser.SIMPLE4_PLENUM_URL);
						simplePlenum4.setType(PlenumSynchronization.PLENUM_TYPE_SIMPLE4);
						plenumSynchronization.insertASimplePlenum(simplePlenum4);
					} catch (Exception e) {
						// publishProgress("Ein Problem ist beim Analysieren der Plenum aufgetreten. Bitte versuchen Sie es später erneut.");
					}
				}
				// DataHolder.dismissProgress();
				DataHolder.releaseScreenLock(PlenumSitzungenActivity.this);
				plenumSynchronization.closeDatabase();
				return null;
			}

		}.execute();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// DataHolder.dismissProgress();
	}

	@Override
	protected void onStart() {
		super.onStart();
		checkShowOptionsMenu(this);
	}

	public void openLine1(View v) {
		if (AppConstant.isFragmentSupported) {
			PlenumObjectViewFragment plenumObjectViewFragment = (PlenumObjectViewFragment) this.getSupportFragmentManager().findFragmentById(R.id.detailsFragment);
			plenumObjectViewFragment.setPlenumObjectType(positionTypes.get(0));
			plenumObjectViewFragment.createPlenumObject();
		} else {
			Intent i = new Intent(this, PlenumObjectViewActivity.class);
			i.putExtra("plenumObjectType", positionTypes.get(0));
			this.startActivity(i);
		}
	}

	public void openLine2(View v) {
		if (AppConstant.isFragmentSupported) {
			PlenumObjectViewFragment plenumObjectViewFragment = (PlenumObjectViewFragment) this.getSupportFragmentManager().findFragmentById(R.id.detailsFragment);
			plenumObjectViewFragment.setPlenumObjectType(positionTypes.get(1));
			plenumObjectViewFragment.createPlenumObject();
		} else {
			Intent i = new Intent(this, PlenumObjectViewActivity.class);
			i.putExtra("plenumObjectType", positionTypes.get(1));
			this.startActivity(i);
		}
	}

	public void openLine3(View v) {
		if (AppConstant.isFragmentSupported) {
			PlenumObjectViewFragment plenumObjectViewFragment = (PlenumObjectViewFragment) this.getSupportFragmentManager().findFragmentById(R.id.detailsFragment);
			plenumObjectViewFragment.setPlenumObjectType(positionTypes.get(2));
			plenumObjectViewFragment.createPlenumObject();
		} else {
			Intent i = new Intent(this, PlenumObjectViewActivity.class);
			i.putExtra("plenumObjectType", positionTypes.get(2));
			this.startActivity(i);
		}
	}

	public void openLine4(View v) {
		if (AppConstant.isFragmentSupported) {
			PlenumObjectViewFragment plenumObjectViewFragment = (PlenumObjectViewFragment) this.getSupportFragmentManager().findFragmentById(R.id.detailsFragment);
			plenumObjectViewFragment.setPlenumObjectType(positionTypes.get(3));
			plenumObjectViewFragment.createPlenumObject();
		} else {
			Intent i = new Intent(this, PlenumObjectViewActivity.class);
			i.putExtra("plenumObjectType", positionTypes.get(3));
			this.startActivity(i);
		}
	}

	public void openOffline(View v) {
		if (AppConstant.isFragmentSupported) {
			PlenumObjectViewFragment plenumObjectViewFragment = (PlenumObjectViewFragment) this.getSupportFragmentManager().findFragmentById(R.id.detailsFragment);
			plenumObjectViewFragment.getPlenumTaskObject();

		}
	}

	/**
	 * Hack to handle the back button.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			if(AppConstant.isFragmentSupported)
				intent.setClass(this, NewsActivityTablet.class);
			else
				intent.setClass(this, NewsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(intent);
			overridePendingTransition(0, 0);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		positionTypes = null;
		// activity = null;
	}
}
