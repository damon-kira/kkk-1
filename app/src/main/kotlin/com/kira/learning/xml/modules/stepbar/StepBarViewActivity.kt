package com.kira.learning.xml.modules.stepbar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.kira.learning.R
import com.kira.learning.databinding.ActivityStepBarBinding
import com.common.base.view.StageStepBar
import com.common.base.viewbinding.binding
import kotlin.getValue

internal class StepBarViewActivity : ComponentActivity() {
    private val mBinding by binding<ActivityStepBarBinding>()
    private val viewModel: ExampleViewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        prepareFields()
        listenToChanges()
        setupListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun prepareFields() {
        mBinding.stepsInStagesTextInputEditText.setText("5,5,5")
        mBinding.nullStateCheckBox.isChecked = true
        mBinding.currentStateStageTextInputLayout.isEnabled = false
        mBinding.currentStateStepTextInputLayout.isEnabled = false
        mBinding.currentStateStageTextInputEditText.setText("2")
        mBinding.currentStateStepTextInputEditText.setText("3")
        mBinding.animationDurationInputEditText.setText("500")

        val orientationItems = listOf("Horizontal", "Vertical")
        val orientationAdapter =
            ArrayAdapter(this, R.layout.item_step_bar, orientationItems)
        mBinding.orientationDropDown.setText("Horizontal")
        mBinding.orientationDropDown.setAdapter(orientationAdapter)

        val hDirectionItems = listOf("Auto", "Left to Right", "Right to Left")
        val hDirectionAdapter = ArrayAdapter(this, R.layout.item_step_bar, hDirectionItems)
        mBinding.horizDirectionDropDown.setText("Auto")
        mBinding.horizDirectionDropDown.setAdapter(hDirectionAdapter)

        val vDirectionItems = listOf("Bottom to top", "Top to Bottom")
        val vDirectionAdapter = ArrayAdapter(this, R.layout.item_step_bar, vDirectionItems)
        mBinding.verticalDirectionDropDown.setText("Bottom to Top")
        mBinding.verticalDirectionDropDown.setAdapter(vDirectionAdapter)

        val drawableItems = listOf("Default Shape", "User Provided")
        val filledTrackAdapter = ArrayAdapter(this, R.layout.item_step_bar, drawableItems)
        mBinding.filledTrackDropDown.setText("Default Shape")
        mBinding.filledTrackDropDown.setAdapter(filledTrackAdapter)

        val unfilledTrackAdapter = ArrayAdapter(this, R.layout.item_step_bar, drawableItems)
        mBinding.unfilledTrackDropDown.setText("Default Shape")
        mBinding.unfilledTrackDropDown.setAdapter(unfilledTrackAdapter)

        val activeThumbListItems = listOf("Null", "Default Shape", "User Provided")
        val activeThumbAdapter = ArrayAdapter(this, R.layout.item_step_bar, activeThumbListItems)
        mBinding.activeThumbDropDown.setText("Null")
        mBinding.activeThumbDropDown.setAdapter(activeThumbAdapter)

        val filledThumbAdapter = ArrayAdapter(this, R.layout.item_step_bar, drawableItems)
        mBinding.filledThumbDropDown.setText("Default Shape")
        mBinding.filledThumbDropDown.setAdapter(filledThumbAdapter)

        val unfilledThumbAdapter = ArrayAdapter(this, R.layout.item_step_bar, drawableItems)
        mBinding.unfilledThumbDropDown.setText("Default Shape")
        mBinding.unfilledThumbDropDown.setAdapter(unfilledThumbAdapter)

        mBinding.thumbSizeSeekBar.progress = 50
        mBinding.thumbSizeValue.text = "${ExampleViewViewModel.Companion.DEFAULT_THUMB_SIZE_DP}dp"
        mBinding.filledTrackSizeSeekBar.progress = 50
        mBinding.filledTrackSizeValue.text =
            "${ExampleViewViewModel.Companion.DEFAULT_FILLED_TRACK_SIZE_DP}dp"
        mBinding.unfilledTrackSizeSeekBar.progress = 50
        mBinding.unfilledTrackSizeValue.text =
            "${ExampleViewViewModel.Companion.DEFAULT_UNFILLED_TRACK_SIZE_DP}dp"

        mBinding.activeThumbDefaultColorView.setBackgroundColor(viewModel.firstActiveThumbColor)
        mBinding.filledThumbDefaultColorView.setBackgroundColor(viewModel.firstFilledThumbColor)
        mBinding.stageStepBar.setFilledThumbToNormalShape(viewModel.firstFilledThumbColor)
        mBinding.unfilledThumbDefaultColorView.setBackgroundColor(viewModel.firstUnfilledThumbColor)
        mBinding.stageStepBar.setUnfilledThumbToNormalShape(viewModel.firstUnfilledThumbColor)
        mBinding.filledTrackDefaultColorView.setBackgroundColor(viewModel.firstFilledTrackColor)
        mBinding.stageStepBar.setFilledTrackToNormalShape(viewModel.firstFilledTrackColor)
        mBinding.unfilledTrackDefaultColorView.setBackgroundColor(viewModel.firstUnfilledTrackColor)
        mBinding.stageStepBar.setUnfilledTrackToNormalShape(viewModel.firstUnfilledTrackColor)

        mBinding.stageStepBar.setDrawTracksBehindThumbs(true)
        mBinding.drawTracksBehindThumbsToggleButton.isChecked = true
    }

    @SuppressLint("SetTextI18n")
    private fun listenToChanges() {
        lifecycleScope.launchWhenStarted {
            viewModel.events.collect { event ->
                when (event) {
                    is Event.AnimateChanged -> {
                        mBinding.animationDurationTextInputLayout.isEnabled = event.animate
                        mBinding.stageStepBar.setAnimate(event.animate)
                    }

                    is Event.AnimationDurationChanged -> {
                        mBinding.stageStepBar.setAnimationDuration(event.animationDuration)
                    }

                    is Event.CurrentStateChanged -> {
                        when (event.change) {
                            is CurrentStateChange.Invalid -> {
                                if (event.change.isForStage) {
                                    mBinding.currentStateStageTextInputLayout.error =
                                        event.change.error
                                } else {
                                    mBinding.currentStateStepTextInputLayout.error =
                                        event.change.error
                                }
                            }

                            is CurrentStateChange.Valid -> {
                                mBinding.currentStateStageTextInputLayout.error = null
                                mBinding.currentStateStepTextInputLayout.error = null
                                mBinding.stageStepBar.setCurrentState(event.change.state)
                                mBinding.currentStateStageTextInputLayout.isEnabled =
                                    event.change.state != null
                                mBinding.currentStateStepTextInputLayout.isEnabled =
                                    event.change.state != null
                            }
                        }
                    }

                    is Event.DrawTracksBehindThumbsChanged -> {
                        mBinding.stageStepBar.setDrawTracksBehindThumbs(event.enabled)
                    }

                    is Event.FilledThumbSetToCustom -> {
                        mBinding.stageStepBar.setFilledThumbToCustomDrawable(event.drawable)
                        setColorSelectorClickable(mBinding.filledThumbDefaultColorView, false)
                    }

                    is Event.FilledThumbSetToDefault -> {
                        mBinding.stageStepBar.setFilledThumbToNormalShape(event.color)
                        mBinding.filledThumbDefaultColorView.setBackgroundColor(event.color)
                        setColorSelectorClickable(mBinding.filledThumbDefaultColorView, true)
                    }

                    is Event.FilledTrackCrossAxisSizeChanged -> {
                        mBinding.stageStepBar.setCrossAxisFilledTrackSize(
                            event.sizeInPixel.dpToPx(
                                this@StepBarViewActivity
                            )
                        )
                        mBinding.filledTrackSizeValue.text = "${event.sizeInPixel}dp"
                    }

                    is Event.FilledTrackSetToCustom -> {
                        mBinding.stageStepBar.setFilledTrackToCustomDrawable(event.drawable)
                        setColorSelectorClickable(mBinding.filledTrackDefaultColorView, false)
                    }

                    is Event.FilledTrackSetToDefault -> {
                        mBinding.stageStepBar.setFilledTrackToNormalShape(event.color)
                        mBinding.filledTrackDefaultColorView.setBackgroundColor(event.color)
                        setColorSelectorClickable(mBinding.filledTrackDefaultColorView, true)
                    }

                    is Event.HorizontalDirectionChanged -> {
                        mBinding.stageStepBar.setHorizontalDirection(event.hDirection)
                    }

                    is Event.OrientationChanged -> {
                        when (event.orientation) {
                            StageStepBar.Orientation.Horizontal -> changeUiToHorizontal()
                            StageStepBar.Orientation.Vertical -> changeUiToVertical()
                        }
                        mBinding.stageStepBar.setOrientation(event.orientation)
                    }

                    is Event.ShowThumbsChanged -> mBinding.stageStepBar.setThumbsVisible(event.enabled)
                    is Event.StepsInStagesConfigChanged -> {
                        when (event.change) {
                            is StepsInStagesChange.Invalid -> {
                                mBinding.stepsInStagesTextInputLayout.error = event.change.error
                            }

                            is StepsInStagesChange.Valid -> {
                                mBinding.stageStepBar.setStageStepConfig(event.change.config)
                                mBinding.stepsInStagesTextInputLayout.error = null
                            }
                        }
                    }

                    is Event.ThumbSizeChanged -> {
                        mBinding.stageStepBar.setThumbSize(event.sizeInPixel.dpToPx(this@StepBarViewActivity))
                        mBinding.thumbSizeValue.text = "${event.sizeInPixel}dp"
                    }

                    is Event.UnfilledThumbSetToCustom -> {
                        mBinding.stageStepBar.setUnfilledThumbToCustomDrawable(event.drawable)
                        setColorSelectorClickable(mBinding.unfilledThumbDefaultColorView, false)
                    }

                    is Event.UnfilledThumbSetToDefault -> {
                        mBinding.stageStepBar.setUnfilledThumbToNormalShape(event.color)
                        mBinding.unfilledThumbDefaultColorView.setBackgroundColor(event.color)
                        setColorSelectorClickable(mBinding.unfilledThumbDefaultColorView, true)
                    }

                    is Event.UnfilledTrackCrossAxisSizeChanged -> {
                        mBinding.stageStepBar.setCrossAxisUnfilledTrackSize(
                            event.sizeInPixel.dpToPx(
                                this@StepBarViewActivity
                            )
                        )
                        mBinding.unfilledTrackSizeValue.text = "${event.sizeInPixel}dp"
                    }

                    is Event.UnfilledTrackSetToCustom -> {
                        mBinding.stageStepBar.setUnfilledTrackToCustomDrawable(event.drawable)
                        setColorSelectorClickable(mBinding.unfilledTrackDefaultColorView, false)
                    }

                    is Event.UnfilledTrackSetToDefault -> {
                        mBinding.stageStepBar.setUnfilledTrackToNormalShape(event.color)
                        mBinding.unfilledTrackDefaultColorView.setBackgroundColor(event.color)
                        setColorSelectorClickable(mBinding.unfilledTrackDefaultColorView, true)
                    }

                    is Event.VerticalDirectionChanged -> {
                        mBinding.stageStepBar.setVerticalDirection(event.vDirection)
                    }

                    is Event.ActiveThumbSetToCustom -> {
                        mBinding.stageStepBar.setActiveThumbToCustomDrawable(event.drawable)
                        setColorSelectorClickable(mBinding.activeThumbDefaultColorView, false)
                    }

                    is Event.ActiveThumbSetToDefault -> {
                        mBinding.stageStepBar.setActiveThumbToNormalShape(event.color)
                        mBinding.activeThumbDefaultColorView.setBackgroundColor(event.color)
                        setColorSelectorClickable(mBinding.activeThumbDefaultColorView, true)
                    }

                    is Event.ActiveThumbSetToNull -> {
                        mBinding.stageStepBar.clearActiveThumb()
                        setColorSelectorClickable(mBinding.activeThumbDefaultColorView, false)
                    }

                    null -> {}
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupListeners() {
        mBinding.closeBtn.setOnClickListener { finish() }

        mBinding.stepsInStagesTextInputEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.stepsInStagesChanged(text.toString())
        }

        mBinding.currentStateStageTextInputEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.currentStageChanged(text.toString())
        }

        mBinding.currentStateStepTextInputEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.currentStepChanged(text.toString())
        }

        mBinding.nullStateCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.stateNullToggled(isChecked)
        }

        mBinding.animateToggleButton.setOnCheckedChangeListener { _, isChecked ->
            viewModel.animateToggled(isChecked)
        }

        mBinding.animationDurationInputEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.animationDurationChanged(text.toString())
        }

        mBinding.orientationDropDown.setOnItemClickListener { _, _, position, _ ->
            viewModel.orientationChanged(position)
        }

        mBinding.horizDirectionDropDown.setOnItemClickListener { _, _, position, _ ->
            viewModel.horizontalDirectionChanged(position)
        }

        mBinding.verticalDirectionDropDown.setOnItemClickListener { _, _, position, _ ->
            viewModel.verticalDirectionChanged(position)
        }

        mBinding.filledTrackDropDown.setOnItemClickListener { _, _, position, _ ->
            viewModel.filledTrackDropdownSelected(position)
        }

        mBinding.unfilledTrackDropDown.setOnItemClickListener { _, _, position, _ ->
            viewModel.unfilledTrackDropdownSelected(position)
        }

        mBinding.filledThumbDropDown.setOnItemClickListener { _, _, position, _ ->
            viewModel.filledThumbDropdownSelected(position)
        }

        mBinding.activeThumbDropDown.setOnItemClickListener { _, _, position, _ ->
            viewModel.activeThumbDropdownSelected(position)
        }

        mBinding.unfilledThumbDropDown.setOnItemClickListener { _, _, position, _ ->
            viewModel.unfilledThumbDropdownSelected(position)
        }

        mBinding.filledTrackDefaultColorView.setOnClickListener { viewModel.filledTrackColorViewClicked() }
        mBinding.unfilledTrackDefaultColorView.setOnClickListener { viewModel.unfilledTrackColorViewClicked() }
        mBinding.activeThumbDefaultColorView.setOnClickListener { viewModel.activeThumbColorViewClicked() }
        setColorSelectorClickable(mBinding.activeThumbDefaultColorView, false)
        mBinding.filledThumbDefaultColorView.setOnClickListener { viewModel.filledThumbColorViewClicked() }
        mBinding.unfilledThumbDefaultColorView.setOnClickListener { viewModel.unfilledThumbColorViewClicked() }

        mBinding.thumbSizeSeekBar.setOnSeekBarChangeListener(object :
            OnSeekBarChangeListenerAdapter() {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.thumbSizeChanged(progress)
            }
        })

        mBinding.filledTrackSizeSeekBar.setOnSeekBarChangeListener(object :
            OnSeekBarChangeListenerAdapter() {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.filledTrackSizeChanged(progress)
            }
        })

        mBinding.unfilledTrackSizeSeekBar.setOnSeekBarChangeListener(object :
            OnSeekBarChangeListenerAdapter() {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.unfilledTrackSizeChanged(progress)
            }
        })

        mBinding.showThumbsToggleButton.setOnCheckedChangeListener { _, isChecked ->
            viewModel.showThumbsToggled(isChecked)
        }

        mBinding.drawTracksBehindThumbsToggleButton.setOnCheckedChangeListener { _, isChecked ->
            viewModel.drawTracksBehindThumbsChanged(isChecked)
        }
    }

    private fun changeUiToHorizontal() {
        mBinding.stageStepBar.updateLayoutParams<ConstraintLayout.LayoutParams> {
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            height =
                resources.getDimensionPixelOffset(R.dimen.stageStepBarSmallDimension)
            width =
                resources.getDimensionPixelOffset(R.dimen.stageStepBarLargeDimension)
        }

        mBinding.mainSeparator.updateLayoutParams<ConstraintLayout.LayoutParams> {
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            topToTop = mBinding.scrollContainer.id
            bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            width = 0
            height = resources.getDimensionPixelOffset(R.dimen.mainSeparatorSize)
        }

        mBinding.scrollContainer.updateLayoutParams<ConstraintLayout.LayoutParams> {
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            startToEnd = ConstraintLayout.LayoutParams.UNSET
            topToTop = ConstraintLayout.LayoutParams.UNSET
            topToBottom = mBinding.stageStepBar.id
        }
    }

    private fun changeUiToVertical() {
        mBinding.stageStepBar.updateLayoutParams<ConstraintLayout.LayoutParams> {
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.UNSET
            height =
                resources.getDimensionPixelOffset(R.dimen.stageStepBarLargeDimension)
            width =
                resources.getDimensionPixelOffset(R.dimen.stageStepBarSmallDimension)
        }

        mBinding.mainSeparator.updateLayoutParams<ConstraintLayout.LayoutParams> {
            startToStart = mBinding.scrollContainer.id
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            topToBottom = mBinding.toolbar.id
            endToEnd = ConstraintLayout.LayoutParams.UNSET
            height = 0
            width = resources.getDimensionPixelOffset(R.dimen.mainSeparatorSize)
        }

        mBinding.scrollContainer.updateLayoutParams<ConstraintLayout.LayoutParams> {
            startToStart = ConstraintLayout.LayoutParams.UNSET
            startToEnd = mBinding.stageStepBar.id
            topToBottom = mBinding.toolbar.id
        }
    }

    private fun setColorSelectorClickable(view: View, isClickable: Boolean) {
        view.isClickable = isClickable
        view.isFocusable = isClickable
        view.isFocusableInTouchMode = isClickable
    }
}

