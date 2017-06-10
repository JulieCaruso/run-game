package adneom.moutons_electriques.game;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.BasicNameValuePair;
import com.koushikdutta.async.http.NameValuePair;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import adneom.moutons_electriques.game.model.User;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoActivity extends AppCompatActivity {

    @BindView(R.id.icon_camera)
    ImageView camera;
    @BindView(R.id.preview)
    ImageView preview;
    @BindView(R.id.photo1)
    ImageView photo1;
    @BindView(R.id.photo2)
    ImageView photo2;
    @BindView(R.id.enter_game)
    Button enterGame;

    private int CAMERA_PIC_REQUEST = 25;
    private int GALLERY_PIC_REQUEST = 30;

    private Uri uri;
    private String file2;
    private Bitmap image;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            user = intent.getExtras().getParcelable(App.EXTRA_USER);
        }

        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);

        displayFiles();

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPersmissions();
            }
        });

        enterGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterGame();
            }
        });
    }

    private void enterGame() {

    }

    private void checkPersmissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //request the missing permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        } else {
            takePhoto();
        }
    }

    private void takePhoto() {
        //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        Intent cameraIntent = new Intent(Intent.ACTION_PICK);
        cameraIntent.setType("image/*");
        //startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        startActivityForResult(cameraIntent, GALLERY_PIC_REQUEST);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            uri = data.getData();
            image = (Bitmap) data.getExtras().get("data");
            preview.setImageBitmap(image);
            //base64
            file2 = transformBase64(image);
            sendFile(file2);

            //createImageFile2();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    //testURL();
                }
            });
            t.start();

        } else if (requestCode == GALLERY_PIC_REQUEST) {
            if (data != null) {
                uri = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, filePath, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                image = BitmapFactory.decodeFile(imagePath, options);

                preview.setImageBitmap(image);
                file2 = transformBase64(image);
                sendFile(file2);
            }
        }
    }

    private String getQuery(List<NameValuePair> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            /*try {
                result.append(URLEncoder.encode(pair.getName(),"UTF-8"));
                result.append(":");
                result.append(URLEncoder.encode(pair.getValue(),"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/
            result.append(pair.getValue());
        }

        Log.i("Adneom", result.toString());
        return result.toString();
    }


    private void sendFile(String file) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("photo", file.replaceAll("(\\r|\\n)", ""));
        Ion.with(this)
                .load(App.URL + "/photo/" + user.getId())
                .setHeader("Content-Type", "application/json")
                .setLogging("ION_LOGGING", Log.VERBOSE)
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.i("Adneom", "error " + (e == null));
                        if (result != null) {
                            if (result.has("photo1")) {
                                if (!result.get("photo1").isJsonNull()) {
                                    user.setPhoto1(result.get("photo1").getAsString());
                                }
                            }
                            if (result.has("photo2")) {
                                if (!result.get("photo2").isJsonNull()) {
                                    user.setPhoto2(result.get("photo2").getAsString());
                                }
                            }
                            displayFiles();
                        }
                    }
                });
    }

    private void displayFiles() {
        if (user.getPhoto1() != null) {
            Picasso.with(this).load(user.getPhoto1())
                    .fit()
                    .into(photo1);
        }
        if (user.getPhoto2() != null) {
            Picasso.with(this).load(user.getPhoto2())
                    .fit()
                    .into(photo2);
        }
    }

    private void updateUser() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", user.getId());
        jsonObject.addProperty("photo1", user.getPhoto1());
        jsonObject.addProperty("photo2", user.getPhoto2());
        jsonObject.addProperty("score", user.getScore());
        Ion.with(this)
                .load(App.URL + "/users/" + user.getId())
                .setHeader("Content-Type", "application/json")
                .setLogging("ION_LOGGING", Log.VERBOSE)
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Log.i("Adneom", "error " + (e == null));
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                }
                break;
        }
    }

    private String transformBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 5, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.CRLF);
        return encoded;
    }

    /********************************
     * TEST CODE
     */

    private void testURL() {
        try {
            URL url = new URL(App.URL + "/photo/687");
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setRequestProperty("Content-Type", "application/json");
            httpsURLConnection.setRequestProperty("Accept", "application/json");

            OutputStream outputSream = httpsURLConnection.getOutputStream();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("photo", file2));
            OutputStream os = httpsURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));
            bufferedWriter.write(getQuery(params));

            //bufferedWriter.flush();
            //bufferedWriter.close();


            /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("photo",byteArray+""));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputSream));
            bufferedWriter.write(getQuery(params));*/

            /*InputStream is = new ByteArrayInputStream(stream.toByteArray());
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                outputSream.write(buffer, 0, len);
            }
            Log.i("Adneom","Tests : "+(is == null)+" && "+(outputSream == null));*/

            String line;
            int responseCcode = httpsURLConnection.getResponseCode();
            Log.i("Adneom", "status is " + responseCcode);
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream(), "UTF-8"));
            while ((line = reader.readLine()) != null) {
                Log.i("Adneom", line);
            }
            reader.close();
            outputSream.close();
            Log.i("Adneom", "12");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

