package com.example.matchinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Integer[] images = {
            R.drawable.image_1,
            R.drawable.image_2,
            R.drawable.image_3,
            R.drawable.image_4,
            R.drawable.image_5,
            R.drawable.image_6,
            R.drawable.image_1,
            R.drawable.image_2,
            R.drawable.image_3,
            R.drawable.image_4,
            R.drawable.image_5,
            R.drawable.image_6
    };

    private List<Integer> imageList = new ArrayList<>(Arrays.asList(images));
    private List<ImageView> flippedTiles = new ArrayList<>();
    private boolean isProcessingClick = false;

    private int imageSize = 180;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayout gridLayout = findViewById(R.id.gridLayout);


        // Shuffle the image list
        Collections.shuffle(imageList);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new GridLayout.LayoutParams());
                imageView.getLayoutParams().width = imageSize;
                imageView.getLayoutParams().height = imageSize;
                imageView.setImageResource(R.color.pink);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.setMargins(10, 20, 10, 10);
                params.height = imageSize;
                params.width = imageSize;
                params.columnSpec = GridLayout.spec(j);
                params.rowSpec = GridLayout.spec(i);
                imageView.setLayoutParams(params);
                imageView.setOnClickListener(new TileClickListener(imageView, i, j));
                gridLayout.addView(imageView);
            }
        }
    }

    private class TileClickListener implements View.OnClickListener {

        private ImageView imageView;
        private int row;
        private int col;

        public TileClickListener(ImageView imageView, int row, int col) {
            this.imageView = imageView;
            this.row = row;
            this.col = col;
        }

        @Override
        public void onClick(View v) {
            // Check if the tile is already flipped or if a click is being processed
            if (flippedTiles.contains(imageView) || isProcessingClick) {
                return;
            }

            // Set the image resource for the ImageView
            int imageResource = imageList.get(row * 3 + col);
            imageView.setImageResource(imageResource);

            // Add the flipped tile to the list
            flippedTiles.add(imageView);

            // Check if two tiles are flipped
            if (flippedTiles.size() == 2) {
                isProcessingClick = true;
                // Get the image resources of the flipped tiles
                int imageResource1 = imageList.get(getPosition(flippedTiles.get(0)));
                int imageResource2 = imageList.get(getPosition(flippedTiles.get(1)));

                // Check if the images match
                if (imageResource1 == imageResource2) {
                    // Images match, keep them flipped
                    Toast.makeText(MainActivity.this, "Correct match of flower!", Toast.LENGTH_SHORT).show();
                    flippedTiles.clear();
                    isProcessingClick = false;
                } else {
                    // Images do not match, flip them back after a delay
                    imageView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            flipTilesBack();
                        }
                    }, 1000);
                }
            }
        }

        private void flipTilesBack() {
            for (ImageView flippedTile : flippedTiles) {
                flippedTile.setImageResource(R.color.pink);
            }
            flippedTiles.clear();
            isProcessingClick = false;
        }
    }

    private int getPosition(ImageView imageView) {
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            if (gridLayout.getChildAt(i) == imageView) {
                return i;
            }
        }
        return -1;
    }
}