package com.kira.learning.module.mathview

import android.media.JetPlayer
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.common.lib.base.BaseActivity
import com.common.lib.viewbinding.binding
import com.kira.learning.R
import com.kira.learning.databinding.ActivityZoomImageBinding
import com.otaliastudios.zoom.ZoomLogger
import com.otaliastudios.zoom.ZoomLogger.Companion.setLogLevel
import com.otaliastudios.zoom.mathview.MathView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ZoomImageActivity : BaseActivity() {
    //    private ExoPlayer player;
    private val mBinding by binding<ActivityZoomImageBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setLogLevel(ZoomLogger.LEVEL_VERBOSE)
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        with(mBinding) {
            mathView2.setMathViewEngine(MathView.MathViewEngine.MATH_JAX)
            zoomImage.setImageDrawable(
                ContextCompat.getDrawable(
                    this@ZoomImageActivity,
                    R.drawable.image_splash_bg
                )
            )

            showZsv.setOnClickListener {
                zoomLayout.visibility = View.GONE
                zoomSurface.visibility = View.GONE
                zoomImage.visibility = View.VISIBLE
            }

            showZl.setOnClickListener {
                zoomImage.visibility = View.GONE
                zoomSurface.visibility = View.GONE
                zoomLayout.visibility = View.VISIBLE

            }

            showZiv.setOnClickListener {
                mathView2.formula = "2a+4b\\sqrt{\\frac{4x-2^{6}}{ax^2+57}}+\\frac{3}{2}"
                zoomImage.visibility = View.GONE
                zoomLayout.visibility = View.GONE
                zoomSurface.visibility = View.VISIBLE
            }
        }

        //        setContentView(R.layout.activity_main);

//        final boolean supportsSurfaceView = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
//        if (supportsSurfaceView) setUpVideoPlayer();

//        final Button buttonZoomLayout = findViewById(R.id.show_zl);
//        final Button buttonZoomImage = findViewById(R.id.show_ziv);
//        final Button buttonZoomSurface = findViewById(R.id.show_zsv);
//        final ZoomLayout zoomLayout = findViewById(R.id.zoom_layout);
//        final ZoomImageView zoomImage = findViewById(R.id.zoom_image);
//        final View zoomSurface = findViewById(R.id.zoom_surface);
//        zoomImage.setImageDrawable(new ColorGridDrawable());
//
//        buttonZoomLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (supportsSurfaceView) player.setPlayWhenReady(false);
//                zoomSurface.setVisibility(View.GONE);
//                zoomImage.setVisibility(View.GONE);
//                zoomLayout.setVisibility(View.VISIBLE);
//                buttonZoomImage.setAlpha(0.65f);
//                buttonZoomSurface.setAlpha(0.65f);
//                buttonZoomLayout.setAlpha(1f);
//            }
//        });

//        buttonZoomImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (supportsSurfaceView) player.setPlayWhenReady(false);
//                zoomSurface.setVisibility(View.GONE);
//                zoomLayout.setVisibility(View.GONE);
//                zoomImage.setVisibility(View.VISIBLE);
//                buttonZoomLayout.setAlpha(0.65f);
//                buttonZoomSurface.setAlpha(0.65f);
//                buttonZoomImage.setAlpha(1f);
//            }
//        });
//        buttonZoomSurface.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (supportsSurfaceView) {
//                    player.setPlayWhenReady(true);
//                    zoomImage.setVisibility(View.GONE);
//                    zoomLayout.setVisibility(View.GONE);
//                    zoomSurface.setVisibility(View.VISIBLE);
//                    buttonZoomLayout.setAlpha(0.65f);
//                    buttonZoomImage.setAlpha(0.65f);
//                    buttonZoomSurface.setAlpha(1f);
//                } else {
//                    Toast.makeText(MainActivity.this,
//                            "ZoomSurfaceView requires API 18", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        buttonZoomLayout.performClick();
    }

    protected override fun onStop() {
        super.onStop()
        //        ZoomSurfaceView surface = findViewById(R.id.surface_view);
//        surface.onPause();
    }


    override fun onStart() {
        super.onStart()
        //        ZoomSurfaceView surface = findViewById(R.id.surface_view);
//        surface.onResume();
    }

    private fun setUpVideoPlayer() {
//        player = new ExoPlayer.Builder(this).build();
//        PlayerControlView controls = findViewById(R.id.player_control_view);
//        final ZoomSurfaceView surface = findViewById(R.id.surface_view);
//        player.addListener(new Player.Listener() {
//            @Override
//            public void onVideoSizeChanged(@NonNull VideoSize videoSize) {
//                surface.setContentSize(videoSize.width, videoSize.height);
//            }
//        });
//        surface.setBackgroundColor(ContextCompat.getColor(this, R.color.background));
//        surface.addCallback(new ZoomSurfaceView.Callback() {
//            @Override
//            public void onZoomSurfaceCreated(@NonNull ZoomSurfaceView view) {
//                player.setVideoSurface(view.getSurface());
//            }
//
//            @Override
//            public void onZoomSurfaceDestroyed(@NonNull ZoomSurfaceView view) { }
//        });
//        controls.setPlayer(player);
//        controls.setShowTimeoutMs(0);
//        controls.show();
//        DataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(this);
//        Uri videoUri = Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
//        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(MediaItem.fromUri(videoUri));
//        player.setMediaSource(videoSource);
//        player.prepare();
    }

    override fun onDestroy() {
        super.onDestroy()
        //        player.release();
    }
}
