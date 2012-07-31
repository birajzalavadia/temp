package de.bundestag.android.helpers;

import java.lang.Thread.State;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

public class ImageThreadLoader
{
    private final HashMap<String, SoftReference<Bitmap>> Cache = new HashMap<String, SoftReference<Bitmap>>();

    private final class QueueItem
    {
        public URL url;
        public ImageLoadedListener listener;
    }

    private final ArrayList<QueueItem> Queue = new ArrayList<QueueItem>();
    private final ArrayList<String> QueueKey = new ArrayList<String>();
    private String CurLoadKey = "";

    private final Handler handler = new Handler(); // Assumes that this is started from the main (UI) thread
    private Thread thread;
    private QueueRunner runner = new QueueRunner();;

    public ImageThreadLoader()
    {
        thread = new Thread(runner);
    }

    public interface ImageLoadedListener
    {
        public void imageLoaded(Bitmap imageBitmap);
    }

    private class QueueRunner implements Runnable
    {
        public void run()
        {
            synchronized (this)
            {
                while (Queue.size() > 0)
                {
                    final QueueItem item = (QueueItem) Queue.remove(0);
                    CurLoadKey = (String) QueueKey.remove(0);

                    // If in the cache, return that copy and be done
                    if (Cache.containsKey(item.url.toString()) && Cache.get(item.url.toString()).get() != null)
                    {
                        // Use a handler to get back onto the UI thread for the
                        // update

                        handler.post(new Runnable()
                        {
                            public void run()
                            {
                                if (item.listener != null)
                                {
                                    // NB: There's a potential race condition
                                    // here where the cache item could get
                                    // garbage collected between when we post
                                    // the runnable and it's executed.
                                    // Ideally we would re-run the network load
                                    // or something.
                                    SoftReference<Bitmap> ref = Cache.get(item.url.toString());
                                    if (ref != null)
                                    {
                                        item.listener.imageLoaded((Bitmap) ref.get());
                                    }
                                }
                            }
                        });
                    }
                    else
                    {
                        final Bitmap bmp = readBitmapFromNetwork(item.url);

                        if (bmp != null)
                        {
                            Cache.put(item.url.toString(), new SoftReference<Bitmap>(bmp));

                            // Use a handler to get back onto the UI thread for
                            // the update
                            handler.post(new Runnable()
                            {
                                public void run()
                                {
                                    if (item.listener != null)
                                    {
                                        item.listener.imageLoaded(bmp);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    public Bitmap loadImage(final String uri, final ImageLoadedListener listener) throws MalformedURLException
    {
//        Log.e("*********", "--1");
        if (Cache.containsKey(uri))
        {
            SoftReference<Bitmap> ref = Cache.get(uri);
            if (ref != null)
            {
                Bitmap tmpbitmap = (Bitmap) ref.get();
                if (tmpbitmap == null)
                {
                    if (!QueueKey.contains(uri) && !CurLoadKey.equals(uri))
                    {
                        QueueItem item = new QueueItem();
                        item.url = new URL(uri);
                        item.listener = listener;
                        QueueKey.add(uri);
                        Queue.add(item);

                        // start the thread if needed
                        if (thread.getState() == State.NEW)
                        {
                            thread.start();
                        }
                        else if (thread.getState() == State.TERMINATED)
                        {
                            thread = new Thread(runner);
                            thread.start();
                        }
                    }
                    return tmpbitmap;
                }
                else
                {
                    return tmpbitmap;
                }
            }
        }

//        Log.e("*********", "--3");
        QueueItem item = new QueueItem();
        item.url = new URL(uri);
        item.listener = listener;
        QueueKey.add(uri);
        Queue.add(item);

        // start the thread if needed
        if (thread.getState() == State.NEW)
        {
            thread.start();
        }
        else if (thread.getState() == State.TERMINATED)
        {
            thread = new Thread(runner);
            thread.start();
        }

        return null;
    }

    public static Bitmap readBitmapFromNetwork(URL url)
    {
        Bitmap bitmap = ImageHelper.loadBitmapFromUrl(url.toString());
        Log.e("yikes", "LOADING IMAGE = " + url.toString());

//         Log.e("*********", "--5");
        return bitmap;
    }

    public void clear()
    {
    }

    public void SetImage(String imgUrl, Bitmap bitmap)
    {
        if (Cache.containsKey(imgUrl))
        {
            Cache.remove(imgUrl);
            Cache.put(imgUrl, new SoftReference<Bitmap>(bitmap));
        }
    }

    public void RemoveIamgeExcept(ArrayList<Bitmap> liveImage)
    {
    }
}