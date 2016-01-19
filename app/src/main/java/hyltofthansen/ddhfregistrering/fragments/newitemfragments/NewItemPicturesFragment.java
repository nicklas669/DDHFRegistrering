package hyltofthansen.ddhfregistrering.fragments.newitemfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hyltofthansen.ddhfregistrering.FragmentDataSingleton;
import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.Singleton;

public class NewItemPicturesFragment extends Fragment {
    private static final int PICK_IMAGE = 100, REQUEST_TAKE_PHOTO = 99;
    ImageView iv_gallery;
    Uri imageUri;
    SharedPreferences prefs;
    private static final String TAG = "NewItemPicturesFragment";
    private String mCurrentPhotoPath;
    private File photoFile;
    private String filePathFromState;
    private File f;
    private int width, height;
    private Bitmap scaledBitmap;

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            filePathFromState = savedInstanceState.getString("filePath");
            if (filePathFromState != null) {
                photoFile = new File(filePathFromState);
            }
            width = savedInstanceState.getInt("width");
            height = savedInstanceState.getInt("height");
            scaledBitmap = savedInstanceState.getParcelable("scaledBitmap");
            if(scaledBitmap != null) {
                iv_gallery.setImageBitmap(scaledBitmap);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(scaledBitmap != null) {
            iv_gallery.setImageBitmap(scaledBitmap);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(photoFile != null) {
            outState.putString("filePath", photoFile.getAbsolutePath());
            outState.putInt("width", width);
            outState.putInt("height", height);
            outState.putSerializable("photoFile", photoFile);
        }
        if (scaledBitmap != null) {
            outState.putParcelable("scaledBitmap", scaledBitmap);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fr_newitem_pictures, container, false); // sæt layout op

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        iv_gallery = (ImageView) root.findViewById(R.id.imgBrowse_galleryView);

        iv_gallery.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                width = iv_gallery.getWidth() / 2; // width is ready
                height = iv_gallery.getHeight() / 2; // height is ready
                Log.d(TAG, "onGlobalLayout(): " + width + " width og height " + height);
            }
        });

        Button b_gallery = (Button) root.findViewById(R.id.imgBrowse_bGallery);
        b_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*");
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        Button b_newImg = (Button) root.findViewById(R.id.imgBrowse_bImage);
        b_newImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "takePictureIntent startes");
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    photoFile = null;
                    try {
                        photoFile = createImageFile();
                        //Log.d(TAG, "photoFile er instantieret...");
                    } catch (IOException ex) {
                        Log.d(TAG, ex.toString());
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        //Log.d(TAG, "putExtra på takePictureIntent");
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                        //Log.d(TAG, "takePictureIntent startActivityForResult");
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }

                } else { // device har ikke kamera features
                    //Viser AlertDialog med teksten "Enheden har ikke kamerafunktionalitet"
                    showNoCameraPopup();
                }
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void showNoCameraPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.intetKameraDialogTekst).setTitle(R.string.intetKameraDialogTitel);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                Log.d(TAG, "intent.getData(): "+intent.getData());
                imageUri = intent.getData();

                //TODO Current min er 15
                String wholeID = DocumentsContract.getDocumentId(imageUri);

                // Split at colon, use second item in the array
                String id = wholeID.split(":")[1];

                String[] column = { MediaStore.Images.Media.DATA };

                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";

                Cursor cursor = getContext().getContentResolver().
                        query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,     //Failure delivering result
                                column, sel, new String[]{ id }, null);

                String filePath = "";
                int columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();

                setPic(iv_gallery, filePath);

                // Gem path til valgt billede
                SharedPreferences.Editor prefedit = prefs.edit();
                Log.d(TAG, "Gemmer chosenImage: "+imageUri.toString()); //Gemmer chosenImage: file:/storage/emulated/0/DCIM/JPEG_20160114_181146_490526767.jpg
                prefedit.putString("chosenImage", imageUri.toString());
                prefedit.commit();
            }
            else if (requestCode == REQUEST_TAKE_PHOTO) {
                galleryAddPic();
                setPic(iv_gallery, photoFile.getAbsolutePath());

                // Gem path til valgt billede
                SharedPreferences.Editor prefedit = prefs.edit();
                Log.d(TAG, "Gemmer chosenImage: "+photoFile.toURI());
                prefedit.putString("chosenImage", photoFile.toURI().toString());
                prefedit.commit();
            }

        }
    }

    /**
     * http://developer.android.com/training/camera/photobasics.html
     */

    private void setPic(ImageView gallery, String path) {

        Log.d(TAG, "path: "+path);

        // Get the dimensions of the View
        int targetW = width;
        int targetH = height;
        Log.d(TAG, "targetWidth: "+targetW+", targetHeight: "+targetH);

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        //BitmapFactory.decodeFile(photoFile.getAbsolutePath(), bmOptions);
        //Log.d(TAG, "path: "+path);
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        Log.d(TAG, "Billede bredde: "+photoW+", højde: "+photoH);

        // Determine how much to scale down the image
        Log.d(TAG, "photoW/targetW: "+photoW/targetW);
        Log.d(TAG, "photoH/targetH: "+photoH/targetH);
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        Log.d(TAG, "scaleFactor: "+scaleFactor);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        //bmOptions.inPurgeable = true;

        scaledBitmap = BitmapFactory.decodeFile(path, bmOptions);

        try {
            ExifInterface ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            Log.d(TAG, orientation + " orientation");

            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    scaledBitmap = rotateImage(scaledBitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    scaledBitmap = rotateImage(scaledBitmap, 180);
                    break;
                // etc.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Ny billede bredde: " + scaledBitmap.getWidth() + ", højde: " + scaledBitmap.getHeight());
        gallery.setImageBitmap(scaledBitmap);
        //return bitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        try {
            f = new File(photoFile.getAbsolutePath());
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);

        } catch (NullPointerException e) {
            f = new File(filePathFromState);
//            Uri contentUri = Uri.fromFile(f);
            Log.d(TAG, e.toString());
        }
        getActivity().sendBroadcast(mediaScanIntent);
    }

    /**
     * http://developer.android.com/training/camera/photobasics.html
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //Log.d(TAG, "storageDir fil oprettes!");
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        //Log.d(TAG, "image fil oprettes!");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
}