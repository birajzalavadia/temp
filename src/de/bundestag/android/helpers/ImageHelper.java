package de.bundestag.android.helpers;

import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Image helper class.
 * 
 * Contains methods to load images and store them in the file system.
 */
public abstract class ImageHelper
{
    public static Bitmap loadBitmapFromUrl(String imageUrl)
    {
        Bitmap bitmap = null;
        if ((imageUrl != null) && (!imageUrl.equals("")))
        {
            try
            {
            	URL url = new URL(imageUrl);
            //	URLConnection connection = url.openConnection();
            //	connection.setUseCaches(false);
                              
                URLConnection conn = null;
                for(int i = 0; i < 5; i++){
             	   conn = url.openConnection();
             	   if(conn.getContentLength()>0)
             		   break;
             	   Log.i("RETRY IMAGE LOAD","RETRY " + (i+1));
                }
               
                FlushedInputStream bitmapStream = new FlushedInputStream((InputStream) conn.getContent());
                
                if (bitmapStream != null)
                {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPurgeable = true;
                    bitmap = BitmapFactory.decodeStream(bitmapStream, null, options);
                }
            } catch (MalformedURLException e)
            {
//                e.printStackTrace();
            } catch (IOException e)
            {
//                e.printStackTrace();
            }
        }

        return bitmap;
    }

    public static Bitmap loadBitmapFromUrlAndResize(Activity activity, String imageUrl)
    {
        Bitmap bitmap = null;
        if ((imageUrl != null) && (!imageUrl.equals("")))
        {
            try
            {
                
            	
            	//FlushedInputStream bitmapStream = new FlushedInputStream((InputStream) new URL(imageUrl).getContent());
            	URL url = new URL(imageUrl);
                URLConnection conn = null;
                for(int i = 0; i < 5; i++){
             	   conn = url.openConnection();
             	   if(conn.getContentLength()>0)
             		   break;
             	   Log.i("RETRY IMAGE LOAD","RETRY " + (i+1));
                }
                
                FlushedInputStream bitmapStream = new FlushedInputStream((InputStream) conn.getContent());

            	if (bitmapStream != null)
                {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPurgeable = true;
                    bitmap = BitmapFactory.decodeStream(bitmapStream, null, options);
                }
            } catch (MalformedURLException e)
            {
//                e.printStackTrace();
            } catch (IOException e)
            {
//                e.printStackTrace();
            }
        }
        if(bitmap!=null)
        	return getScaleImageURL(activity, bitmap);
        else
        	return null;
    }

    static class FlushedInputStream extends FilterInputStream
    {
        public FlushedInputStream(InputStream inputStream)
        {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException
        {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n)
            {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L)
                {
                    int bytes = read();
                    if (bytes < 0)
                    {
                        break; // we reached EOF
                    }
                    else
                    {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }

    public static Bitmap convertStringToBitmap(String imageString)
    {
        Bitmap decodedByte = null;
        if (imageString != null)
        {
            byte[] imageAsBytes;
            try
            {
                imageAsBytes = Base64.decode(imageString.getBytes());
                decodedByte = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            } catch (IOException e)
            {
//                e.printStackTrace();
            }
        }

        return decodedByte;
    }

    public static String convertBitmapToString(Bitmap imageBitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] image = baos.toByteArray();

        return Base64.encodeBytes(image);
    }

    public static float getBitmapScalingFactor(Activity activity, Bitmap bm)
    {
        // Get display width from device
    	//When width bigger than 550 limit the image scale
        int displayWidth = Math.min(480, activity.getWindowManager().getDefaultDisplay().getWidth());

        // Calculate the max width of the imageView
        int imageViewWidth = displayWidth;

        // Calculate scaling factor and return it
        return ((float) imageViewWidth / (float) bm.getWidth());
    }

    public static Bitmap getScaleImage(Activity activity, String imageString)
    {
        Bitmap bm = convertStringToBitmap(imageString);
        float scalingFactor = getBitmapScalingFactor(activity, bm);
        int scaleHeight = (int) (bm.getHeight() * scalingFactor);
        int scaleWidth = (int) (bm.getWidth() * scalingFactor);

        return Bitmap.createScaledBitmap(bm, scaleWidth, scaleHeight, true);
    }

    public static Bitmap getScaleImageURL(Activity activity, Bitmap bm)
    {
        float scalingFactor = getBitmapScalingFactor(activity, bm);
        int scaleHeight = (int) (bm.getHeight() * scalingFactor);
        int scaleWidth = (int) (bm.getWidth() * scalingFactor);

        return Bitmap.createScaledBitmap(bm, scaleWidth, scaleHeight, true);
    }
}
