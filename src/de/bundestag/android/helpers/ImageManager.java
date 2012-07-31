package de.bundestag.android.helpers;

import java.io.File;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import de.bundestag.android.storage.NewsDatabaseAdapter;

public class ImageManager
{
    public static final String KEY_IMAGEURL = "KEY_IMAGEURL";

    public static final String KEY_NEWSID = "KEY_NEWSID";

    private HashMap<String, SoftReference<Bitmap>> imageMap = new HashMap<String, SoftReference<Bitmap>>();

    private File cacheDir;
    private ImageQueue imageQueue = new ImageQueue();
    private Thread imageLoaderThread = new Thread(new ImageQueueManager());
    private Context context;

    public ImageManager(Context context)
    {
        // Make background thread low priority, to avoid affecting UI
        // performance
        imageLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);

        // Find the dir to save cached images
        String sdState = android.os.Environment.getExternalStorageState();
        if (sdState.equals(android.os.Environment.MEDIA_MOUNTED))
        {
            File sdDir = android.os.Environment.getExternalStorageDirectory();
            cacheDir = new File(sdDir, "data/codehenge");
        }
        else
        {
            cacheDir = context.getCacheDir();
        }

        if (!cacheDir.exists())
        {
            cacheDir.mkdirs();
        }

        this.context = context;
    }

    public void displayImage(String url, Activity activity, ImageView imageView)
    {
        if (imageMap.containsKey(url))
        {
            imageView.setImageBitmap(imageMap.get(url).get());
        }
        else
        {
            queueImage(url, activity, imageView);
            // imageView.setImageResource(R.drawable.icon);
        }
    }

    private void queueImage(String url, Activity activity, ImageView imageView)
    {
        // This ImageView might have been used for other images, so we clear
        // the queue of old tasks before starting.
        imageQueue.Clean(imageView);
        ImageRef p = new ImageRef(url, imageView);

        synchronized (imageQueue.imageRefs)
        {
            imageQueue.imageRefs.push(p);
            imageQueue.imageRefs.notifyAll();
        }

        // Start thread if it's not started yet
        if (imageLoaderThread.getState() == Thread.State.NEW)
        {
            imageLoaderThread.start();
        }
    }

    private Bitmap getBitmap(String newsId, String url)
    {
        // String filename = String.valueOf(url.hashCode());
        // File f = new File(cacheDir, filename);
        //
        // System.out.println("URL = " + url);
        // // Is the bitmap in our cache?
        // Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());
        // if (bitmap != null)
        // {
        // return bitmap;
        // }

        // Nope, have to download it
        try
        {
            Bitmap bitmap = BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
            // save bitmap to cache for later
            // writeFile(bitmap, f);
            storeNewsBitmap(newsId, bitmap);

            return bitmap;
        } catch (Exception ex)
        {
//            ex.printStackTrace();
            return null;
        }
    }

    /** Classes **/

    private class ImageRef
    {
        public String url;
        public ImageView imageView;

        public ImageRef(String u, ImageView i)
        {
            url = u;
            imageView = i;
        }
    }

    // stores list of images to download
    private class ImageQueue
    {
        private Stack<ImageRef> imageRefs = new Stack<ImageRef>();

        // removes all instances of this ImageView
        public void Clean(ImageView view)
        {
            for (int i = 0; i < imageRefs.size();)
            {
                if (imageRefs.get(i).imageView == view)
                {
                    imageRefs.remove(i);
                }
                else
                {
                    ++i;
                }
            }
        }
    }

    private class ImageQueueManager implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                while (true)
                {
                    // Thread waits until there are images in the
                    // queue to be retrieved
                    if (imageQueue.imageRefs.size() == 0)
                    {
                        synchronized (imageQueue.imageRefs)
                        {
                            imageQueue.imageRefs.wait();
                        }
                    }

                    // When we have images to be loaded
                    if (imageQueue.imageRefs.size() != 0)
                    {
                        ImageRef imageToLoad;

                        synchronized (imageQueue.imageRefs)
                        {
                            imageToLoad = imageQueue.imageRefs.pop();
                        }

                        HashMap<String, String> imageData = (HashMap<String, String>) imageToLoad.imageView.getTag();

                        Bitmap bitmap = getBitmap((String) imageData.get(KEY_NEWSID), imageToLoad.url);
                        imageMap.put(imageToLoad.url, new SoftReference<Bitmap>(bitmap));

                        // Object tag = imageToLoad.imageView.getTag(1);
                        // String tag = (String) imageData.get(KEY_IMAGEURL);

                        // Make sure we have the right view - thread safety
                        // defender
                        // if (tag != null && ((String)
                        // tag).equals(imageToLoad.url))
                        // {
                        // BitmapDisplayer bmpDisplayer = new
                        // BitmapDisplayer(bitmap, imageToLoad.imageView);
                        //
                        // Activity a = (Activity)
                        // imageToLoad.imageView.getContext();
                        //
                        // a.runOnUiThread(bmpDisplayer);
                        // }
                    }

                    if (Thread.interrupted())
                    {
                        break;
                    }
                }
            } catch (InterruptedException e)
            {
//                e.printStackTrace();
            }
        }
    }

    private void storeNewsBitmap(String newsId, Bitmap bitmap)
    {
        NewsDatabaseAdapter newsDatabaseAdapter = new NewsDatabaseAdapter(context);
        newsDatabaseAdapter.open();

        newsDatabaseAdapter.updatePicture(newsId, ImageHelper.convertBitmapToString(bitmap));

        newsDatabaseAdapter.close();
    }
}