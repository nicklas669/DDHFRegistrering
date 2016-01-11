package hyltofthansen.ddhfregistrering.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import hyltofthansen.ddhfregistrering.R;

public class NewItemPicturesFragment extends Fragment {
    private static final int PICK_IMAGE = 100, REQUEST_TAKE_PHOTO = 99;
    ImageView iv_gallery;
    Uri imageUri;
    SharedPreferences prefs;
    private static final String TAG = "NewItemPicturesFragment";
    private String mCurrentPhotoPath;
    private File photoFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Vedhæft billede");

//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); // aktivér "tilbage"-pil i venstre top

        View root = inflater.inflate(R.layout.newitempictures, container, false); // sæt layout op

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        iv_gallery = (ImageView) root.findViewById(R.id.imgBrowse_galleryView);

        //Læs om der er valgt et billede i forvejen
        String imgURI = prefs.getString("chosenImage", null);
        if (imgURI != null) {
            Log.d(TAG, "Læser chosenImage: "+imgURI);
            // Hvis der er et valgt billede så vis det i imageViewet
            imageUri = Uri.parse(imgURI);
            InputStream imageStream = null;
            try {
                imageStream = getActivity().getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
            iv_gallery.setImageBitmap(imageBitmap);
        }

        Button b_gallery = (Button) root.findViewById(R.id.imgBrowse_bGallery);
        b_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.intetKameraDialogTekst).setTitle(R.string.intetKameraDialogTitel);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                Log.d(TAG, "intent.getData(): "+intent.getData());
                imageUri = intent.getData();

                String wholeID = DocumentsContract.getDocumentId(imageUri);

                // Split at colon, use second item in the array
                String id = wholeID.split(":")[1];

                String[] column = { MediaStore.Images.Media.DATA };

                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";

                Cursor cursor = getContext().getContentResolver().
                        query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
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
                Log.d(TAG, "Gemmer chosenImage: "+imageUri.toString());
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
//        InputStream imageStream = null;
//        try {
//            imageStream = getActivity().getContentResolver().openInputStream(imageUri);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);

        if (path.contains("content:")) {
            path = getFilePathFromContentUri(Uri.parse(path), getContext().getContentResolver());
            Log.d(TAG, "path er content, ny path er: "+path);
        }

        // Get the dimensions of the View
        int targetW = gallery.getWidth();
        int targetH = gallery.getHeight();
        Log.d(TAG, "imageView bredde: "+targetW+", højde: "+targetH);

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
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        Log.d(TAG, "Ny billede bredde: "+bitmap.getWidth()+", højde: "+bitmap.getHeight());
        gallery.setImageBitmap(bitmap);
        //return bitmap;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(photoFile.getAbsolutePath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
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
        Log.d(TAG, "storageDir fil oprettes!");
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        Log.d(TAG, "image fil oprettes!");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    /**
     * Gets the corresponding path to a file from the given content:// URI
     * @param selectedImageUri The content:// URI to find the file path from
     * @param contentResolver The content resolver to use to perform the query.
     * @return the file path as a string
     */
    private String getFilePathFromContentUri(Uri selectedImageUri,
                                             ContentResolver contentResolver) {
        //Log.d(TAG, "selectedImageUri: "+selectedImageUri.toString());
        //        String filePath;
//        String[] filePathColumn = {MediaStore.MediaColumns.DATA};
//        Log.d(TAG, filePathColumn[0]);
//
//        Cursor cursor = contentResolver.query(selectedImageUri, filePathColumn, null, null, null);
//        cursor.moveToFirst();
//        Log.d(TAG, "cursor.getString(0): " + cursor.getString(0));
//
//        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//        Log.d(TAG, "columnIndex: " + columnIndex);
//        filePath = cursor.getString(columnIndex);
//        cursor.close();
//        Log.d(TAG, "filePath returneres: " + filePath);
        return "";
    }
}