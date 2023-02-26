package com.pollub.firstandroidmlproject;

//import static com.pollub.firstandroidmlproject.R.id.imageView;
import static com.pollub.firstandroidmlproject.R.id.imageView_ss;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.pollub.firstandroidmlproject.ml.KerasModel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

//import java.io.IOError;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_VIDEO_PICKER = 1;

    Button selectVideoButton;
     VideoView videoView;
     TextView Showresult;
     String buffet_txt;
     ImageView ss_show;
    public int[] arr_formodel=new int[4];
    private MediaMetadataRetriever retriever;
    private Context context;


    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectVideoButton = findViewById(R.id.select_video_button);
        videoView = findViewById(R.id.video_view);
        Showresult = findViewById(R.id.show_result);
        ss_show=findViewById(R.id.imageView_ss);

        selectVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_VIDEO_PICKER);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_VIDEO_PICKER && resultCode == RESULT_OK && data != null) {
                Uri videoUri = data.getData();


                // Initialize the MediaMetadataRetriever
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(MainActivity.this, videoUri);

                // Get a frame from the video
                long timeUs = 3000000; // Choose the time (in microseconds) at which to extract the frame
                Bitmap frame = retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

                // Release the MediaMetadataRetriever
                retriever.release();

                // Display the screenshot in the ImageView

                ss_show.setImageBitmap(frame);




                /*


                // Display the selected video
                videoView.setVideoURI(videoUri);
                videoView.setMediaController(new MediaController(this));
                videoView.requestFocus();
                videoView.start();
                arr_formodel = new int[]{1, 10, 224, 224, 3};

                 */

            /*
            // Do something with the selected video
            try {
                KerasModel model = KerasModel.newInstance(MainActivity.this);

                // Creates inputs for reference.
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 10, 224, 224, 3}, DataType.FLOAT32);

                inputFeature0.loadBuffer(uriToByteBuffer_short(videoUri),arr_formodel);

                // Runs model inference and gets result.
                KerasModel.Outputs outputs = model.process(inputFeature0);
                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                //Showresult.setText(outputFeature0.toString());



                // Releases model resources if no longer used.
                model.close();
            } catch (IOException e) {
                Showresult.setText(e.toString());

            }

             */

        }
    }
    private void displayVideo(Uri videoUri) {

/*
        //We could change out video to buffer type and we showed in txt box
        try {
            buffet_txt=uriToByteBuffer_short(videoUri).toString();
            Showresult.setText(buffet_txt);


        }
        catch (IOException e){
            Showresult.setText(e.toString());

        }
        */

        videoView.setVideoURI(videoUri);
        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();
        videoView.start();
        arr_formodel=new int[]{1, 10, 224, 224, 3};

        // Do something with the selected video
        try {
            KerasModel model = KerasModel.newInstance(MainActivity.this);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 10, 224, 224, 3}, DataType.FLOAT32);

            inputFeature0.loadBuffer(uriToByteBuffer_short(videoUri),arr_formodel);

            // Runs model inference and gets result.
            KerasModel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            //Showresult.setText(outputFeature0.toString());



            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            Showresult.setText(e.toString());

        }




/*
        try {
            playVideoFromByteBuffer(uriToByteBuffer(videoUri));
        } catch (IOException e) {
            e.printStackTrace();
        }
*/


/*

        videoView.setVideoURI(videoUri);
        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();
        videoView.start();
*/



    }



    public ByteBuffer uriToByteBuffer_short(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);

        ByteArrayOutputStream byteBufferOutputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteBufferOutputStream.write(buffer, 0, bytesRead);
        }

        byte[] bytes = byteBufferOutputStream.toByteArray();

        return ByteBuffer.wrap(bytes);
    }
    private ByteBuffer uriToByteBuffer(Uri uri) throws IOException {
        // Open an input stream to the URI
        InputStream inputStream = getContentResolver().openInputStream(uri);

        // Read the contents of the file into a byte array
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, bytesRead);
        }
        byte[] bytes = byteBuffer.toByteArray();

        // Convert the byte array to a ByteBuffer
        ByteBuffer byteBufferr = ByteBuffer.allocateDirect(bytes.length);
        byteBufferr.order(ByteOrder.nativeOrder());
        byteBufferr.put(bytes);
        byteBufferr.flip();

        // Reshape the buffer to the required dimensions
        int[] shape = new int[]{1, 10, 224, 224, 3};
        float[] data = new float[shape[0] * shape[1] * shape[2] * shape[3] * shape[4]];
        for (int i = 0; i < data.length; i++) {
            data[i] = (float) (byteBufferr.get() & 0xFF) / 255.0f;
        }
        byteBufferr = ByteBuffer.allocateDirect(data.length * 4);
        byteBufferr.order(ByteOrder.nativeOrder());
        for (int i = 0; i < data.length; i++) {
            byteBufferr.putFloat(data[i]);
        }
        byteBufferr.flip();

        return byteBufferr;
    }
    private void playVideoFromByteBuffer(ByteBuffer byteBuffer) {
        // Convert the ByteBuffer to a byte array
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);

        // Create an InputStream from the byte array
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        // Set the InputStream as the video source for the VideoView
        VideoView videoView = findViewById(R.id.video_view);
        videoView.setVideoURI(Uri.parse("dummy-uri.mp4"));

        // Start playback
        videoView.start();
    }

    //İSTENİLEN BOYUTLARA ÖZGÜ BİR VİDEO TELEFONA İNDİRİP ONUN ÜZERİNDE MODEL TEST EDİLEBİLİR


}
