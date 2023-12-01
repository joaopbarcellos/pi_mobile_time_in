package fellipy.gustavo.joao_pedro.pedro.time_in;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.ImageView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fellipy.gustavo.joao_pedro.pedro.time_in.util.Config;
import fellipy.gustavo.joao_pedro.pedro.time_in.util.HttpRequest;
import fellipy.gustavo.joao_pedro.pedro.time_in.util.Util;

/**
 * A classe ImageCache realiza o download de imagens e as seta em um dado ImageView. Toda imagem
 * obtida é salva no cache da app. Dessa forma, a imagem só é transferida uma única vez.
 */
public class ImageCache {

    /**
     * Realiza o download de uma imagem presente em um endereço URL fornecido e a salva no cache
     * @param context contexto da app
     * @param imageUrl endereço URL da imagem
     * @param imageView ImageView onde a imagem deverá aparecer na app
     * @param w largura que a imagem deve ter
     * @param h altura que a imagem deve ter
     */
    public static String loadImageUrlToImageView(Context context, String imageUrl, ImageView imageView, int w, int h) {

        URL imgUrl = null;
        try {
            imgUrl = new URL(imageUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace(System.out);
            return "";
        }
        String path = imgUrl.getPath();
        String imageName = path.replace('/', '-');
        String imageLocation = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imageName;

        File f = new File(imageLocation);
        if (f.exists() && !f.isDirectory()) {
            imageView.setImageBitmap(Util.getBitmap(imageLocation, w, h));
        }
        else {
            MutableLiveData<Bitmap> imgLd = new MutableLiveData<>();

            imgLd.observe(getLifecycleOwner(context), new Observer<Bitmap>() {
                @Override
                public void onChanged(Bitmap bitmap) {
                    imageView.setImageBitmap(bitmap);
                }
            });
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    HttpRequest httpRequest = new HttpRequest(imageUrl.toString(), "GET", "UTF-8");

                    try {
                        InputStream is = httpRequest.execute();
                        Bitmap img = Util.inputStream2Bitmap(is);
                        httpRequest.finish();

                        Util.saveImage(img, imageLocation);

                        Bitmap finalImg = Util.getBitmap(imageLocation, w, h);
                        imgLd.postValue(finalImg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return imageLocation;
    }

    /**
     * Realiza o download de uma imagem presente em um bd. A imagem é obtida via id e é salva no cache
     * @param context contexto da app
     * @param id id da imagem no bd do servidor
     * @param imageView ImageView onde a imagem deverá aparecer na app
     * @param w largura que a imagem deve ter
     * @param h altura que a imagem deve ter
     */
    public static String loadImageBase64ToImageView(Context context, String id, ImageView imageView, int w, int h) {

        String imageLocation = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + id;
        File f = new File(imageLocation);
        if (f.exists() && !f.isDirectory()) {
            imageView.setImageBitmap(Util.getBitmap(imageLocation, w, h));
        }
        else {
            MutableLiveData<Bitmap> imgLd = new MutableLiveData<>();
            imgLd.observe(getLifecycleOwner(context), new Observer<Bitmap>() {
                @Override
                public void onChanged(Bitmap bitmap) {
                    imageView.setImageBitmap(bitmap);
                }
            });

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL + "pegar_imagem_produto.php", "GET", "UTF-8");
                    httpRequest.addParam("id", id);

                    try {
                        InputStream is = httpRequest.execute();
                        String imgBase64 = Util.inputStream2String(is, "UTF-8");
                        httpRequest.finish();

                        String pureBase64Encoded = imgBase64.substring(imgBase64.indexOf(",") + 1);
                        Bitmap img = Util.base642Bitmap(pureBase64Encoded);
                        Util.saveImage(img, imageLocation);

                        Bitmap finalImg = Util.getBitmap(imageLocation, w, h);
                        imgLd.postValue(finalImg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return imageLocation;
    }


    private static LifecycleOwner getLifecycleOwner(Context context) {
        while (!(context instanceof LifecycleOwner)) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        return (LifecycleOwner) context;
    }
}
