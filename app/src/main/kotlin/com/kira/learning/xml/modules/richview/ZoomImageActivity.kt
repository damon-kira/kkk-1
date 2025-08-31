package com.kira.learning.xml.modules.richview

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.common.lib.base.BaseActivity
import com.common.lib.viewbinding.binding
import com.kira.learning.R
import com.kira.learning.databinding.ActivityZoomImageBinding
import com.kira.richtext.Attachment
import com.kira.richtext.Tokenizer
import com.otaliastudios.zoom.ZoomLogger
import com.otaliastudios.zoom.ZoomLogger.Companion.setLogLevel
import com.otaliastudios.zoom.mathview.MathView
import dagger.hilt.android.AndroidEntryPoint
import org.scilab.forge.jlatexmath.core.AjLatexMath

@AndroidEntryPoint
class ZoomImageActivity : BaseActivity() {
    //    private ExoPlayer player;
    private val mBinding by binding<ActivityZoomImageBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setLogLevel(ZoomLogger.LEVEL_VERBOSE)
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        initRichText()
        with(mBinding) {
            mathView2.setMathViewEngine(MathView.MathViewEngine.MATH_JAX)
            zoomImage.setImageDrawable(
                ContextCompat.getDrawable(
                    this@ZoomImageActivity,
                    R.drawable.image_splash_bg
                )
            )
            rtButton.setOnClickListener {
                zoomImage.visibility = View.GONE
                zoomSurface.visibility = View.GONE
                ftFrameLayout.visibility = View.VISIBLE

            }

            mathButton.setOnClickListener {
                mathView2.formula = "2a+4b\\sqrt{\\frac{4x-2^{6}}{ax^2+57}}+\\frac{3}{2}"
                zoomImage.visibility = View.GONE
                ftFrameLayout.visibility = View.GONE
                zoomSurface.visibility = View.VISIBLE
            }
            zoomButton.setOnClickListener {
                ftFrameLayout.visibility = View.GONE
                zoomSurface.visibility = View.GONE
                zoomImage.visibility = View.VISIBLE
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

    private fun initRichText() {
//        CodeProcessor.init(this)
        AjLatexMath.init(this) // init library: load fonts, create paint, etc.

        val attachments: MutableList<Attachment?> = ArrayList<Attachment?>()
        attachments.add(
            ExampleAttachment(
                "Android Image",
                "53ce1",
                true,
                "https://framerusercontent.com/images/jKhc6WcpixtwqG54Jy46cv0RShQ.png"
            )
        )
        attachments.add(
            ExampleAttachment(
                "Here is a link",
                "bc41a",
                false,
                "https://www.baidu.com"
            )
        )

        Tokenizer.setCenterStartLabels("<center>")
        Tokenizer.setCenterEndLabels("</center>")
        Tokenizer.setTitleStartLabels("<h>")
        Tokenizer.setTitleEndLabels("</h>")

        mBinding.ftText.setText(
            "<h><center>hi!</center></h>" +
                    "[quote]This is quote\n" +
                    "second line\n" +
                    "third line\n" +
                    "One\n" +
                    "Two\n" +
                    "Three\n" +
                    "fourth line[/quote]" +
                    "Here is an attachment:[attachment:53ce1]" +
                    "[code]" +
                    getCodeString() +
                    "[/code]" +
                    "Hello FlexibleRichTextView!\n" +
                    "This is LaTeX:\n" +
                    "\$e^{\\pi i} + 1 = 0$\n" +
                    "$$ x = \\frac{-b \\pm \\sqrt{b^2 - 4ac}}{2a} $$ \n" +
                    "This is table:\n" +
                    "| First Header  | Second Header |\n" +
                    "| --- | --- |\n" +
                    "| Content Cell  | Content Cell  |\n" +
                    "| Content Cell  | Content Cell  |\n" +
                    "An attachment is shown at the bottom: \n",
            attachments
        )
//        mBinding.ftText.setText(
//
//            "<h><center>hi!</center></h>\n" +
//                    "This is LaTeX:\n" +
//                    "\$e^{\\pi i} + 1 = 0$\n" +
//                    "$$ x = \\frac{-b \\pm \\sqrt{b^2 - 4ac}}{2a} $$ \n", attachments
//        )
//        mBinding.ftText.setText(
//            """
//            <h><center>hi!</center></h>[quote]This is quote
//                    second line
//                    third line
//                    One
//                    Two
//                    Three
//                    fourth line[/quote]
//                     """.trimIndent(), attachments
//        )

    }

    fun getCodeString(): String {
        return """
        import android.os.Bundle;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import android.util.Log;

        import org.jetbrains.annotations.NotNull;

        import io.github.kbiakov.codeview.CodeView;
        import io.github.kbiakov.codeview.OnCodeLineClickListener;
        import io.github.kbiakov.codeview.adapters.CodeWithDiffsAdapter;
        import io.github.kbiakov.codeview.adapters.Options;
        import io.github.kbiakov.codeview.highlight.ColorTheme;
        import io.github.kbiakov.codeview.highlight.ColorThemeData;
        import io.github.kbiakov.codeview.highlight.Font;
        import io.github.kbiakov.codeview.highlight.FontCache;
        import io.github.kbiakov.codeview.views.DiffModel;

        public class ListingsActivity extends AppCompatActivity {

            @Override
            protected void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_listings);

                final CodeView codeView = (CodeView) findViewById(R.id.code_view);

                /*
                 * 1: set code content
                 */

                // auto language recognition
                codeView.setCode(getString(R.string.listing_js));

                // specify language for code listing
                codeView.setCode(getString(R.string.listing_py), "py");
            }
        }
    """.trimIndent()
    }

    override fun onStop() {
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
