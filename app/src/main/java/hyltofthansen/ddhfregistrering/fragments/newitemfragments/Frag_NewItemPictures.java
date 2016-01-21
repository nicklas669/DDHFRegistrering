package hyltofthansen.ddhfregistrering.fragments.newitemfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
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
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hyltofthansen.ddhfregistrering.ImgRotationDetection;
import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.activities.Act_ShowImage;
import hyltofthansen.ddhfregistrering.adapters.Adapter_NewItemPicture;
import hyltofthansen.ddhfregistrering.singletons.Sing_NewItemData;

public class Frag_NewItemPictures extends Fragment {
    private static final int PICK_IMAGE = 100, REQUEST_TAKE_PHOTO = 99;
    ImageView iv_gallery;
    Uri imageUri;
    SharedPreferences prefs;
    private static final String TAG = "Frag_NewItemPictures";
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
        GridView gridview = (GridView) root.findViewById(R.id.gridview_newitem_pics);

        final Adapter_NewItemPicture adapter_newItemPicture = new Adapter_NewItemPicture(getContext());

        gridview.setAdapter(adapter_newItemPicture);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Sing_NewItemData.getInstance().deletePhotoFilePathItem(position);
                                adapter_newItemPicture.notifyDataSetChanged();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Vil du slette billede " + position + "?").setPositiveButton("Ja", dialogClickListener)
                        .setNegativeButton("Nej", dialogClickListener).show();
            }
        });

//        iv_gallery.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                width = iv_gallery.getWidth() / 2; // width is ready
//                height = iv_gallery.getHeight() / 2; // height is ready
//                Log.d(TAG, "onGlobalLayout(): " + width + " width og height " + height);
//            }
//        });

//        Button b_gallery = (Button) root.findViewById(R.id.imgBrowse_bGallery);
//        b_gallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent gallery = null;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//                    gallery = new Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*");
//                    startActivityForResult(gallery, PICK_IMAGE);
//                }
//            }
//        });

        Button b_newImg = (Button) root.findViewById(R.id.imgBrowse_bImage);
        b_newImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "takePictureIntent startes");
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Log.d(TAG, ex.toString());
                    }
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
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

//                setPic(iv_gallery, filePath);

                // Gem path til valgt billede
                SharedPreferences.Editor prefedit = prefs.edit();
                Log.d(TAG, "Gemmer chosenImage: "+imageUri.toString()); //Gemmer chosenImage: file:/storage/emulated/0/DCIM/JPEG_20160114_181146_490526767.jpg
                prefedit.putString("chosenImage", imageUri.toString());
                prefedit.commit();
            }
            else if (requestCode == REQUEST_TAKE_PHOTO) {
                galleryAddPic();
//                setPic(iv_gallery, photoFile.getAbsolutePath());
                Sing_NewItemData.getInstance().addPhotoFilePath(photoFile.getAbsolutePath());


                // Gem path til valgt billede
                SharedPreferences.Editor prefedit = prefs.edit();
                Log.d(TAG, "Gemmer chosenImage: "+photoFile.toURI());
                prefedit.putString("chosenImage", photoFile.toURI().toString());
                prefedit.commit();
            }

        }
    }

    /**
     * Initiate media scanning so picture taken with camera appears on the phone's gallery
     */
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
        File storageDir = Environment.getExternalStorageDirectory();
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