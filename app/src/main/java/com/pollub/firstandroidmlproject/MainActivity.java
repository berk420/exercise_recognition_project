package com.pollub.firstandroidmlproject;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pollub.firstandroidmlproject.ml.KerasModel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

//import java.io.IOError;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btLoadImage, btCaptureImage;
    TextView tvResult;
    ImageView ivAddImage;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> mGetContent;
    private static final int REQUEST_VIDEO_PICKER = 1;
    private Button selectVideoButton;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivAddImage=findViewById(R.id.iv_add_image);
        tvResult=findViewById(R.id.tv_result);
        btLoadImage=findViewById(R.id.bt_load_image);
        btCaptureImage=findViewById(R.id.bt_capture_image);

        
        selectVideoButton = findViewById(R.id.select_video_button);

        selectVideoButton.setOnClickListener(new View.OnClickListener() {
                   @Override
        public void onClick(View v) {
                  Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                  startActivityForResult(intent, REQUEST_VIDEO_PICKER);
              }});

        mGetContent=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Bitmap imageBitmap =null;
                try {
                    imageBitmap=UriToBitmap(result);
                } catch (IOException e){
                    e.printStackTrace();
                }
                ivAddImage.setImageBitmap(imageBitmap);
                outputGenerator(imageBitmap);

                Log.d("TAG_URI",""+result);
            }
        });

        btLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/* ");
            }
        });


        tvResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.google.com/search?q="+tvResult.getText().toString()));
                startActivity(intent);
            }
        });

    }

    private void outputGenerator(Bitmap imageBitmap) {
        try {
            //BirdsModel model = BirdsModel.newInstance(MainActivity.this);
            KerasModel model = KerasModel.newInstance(MainActivity.this);

            // Creates inputs for reference.
            //TensorImage image = TensorImage.fromBitmap(imageBitmap);
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 10, 224, 224, 3}, DataType.FLOAT32);
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            //BirdsModel.Outputs outputs = model.process(image);
            //List<Category> probability = outputs.getProbabilityAsCategoryList();
            KerasModel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();


          //  int index =0;
          //  float max = probability.get(0).getScore();

          //  for(int i=0;i<probability.size();i++){
          //      if (max<probability.get(i).getScore()){
          //          max=probability.get(i).getScore();
           //         index=i;
           //     }
            //}
            //Category output=probability.get(index);

            //tvResult.setText(output.getLabel());

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }

    }

    private Bitmap UriToBitmap(Uri result) throws IOException {
        return MediaStore.Images.Media.getBitmap(this.getContentResolver(),result);

    }
}