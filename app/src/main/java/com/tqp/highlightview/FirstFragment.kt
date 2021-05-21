package com.tqp.highlightview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.tqp.guideview.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var mTAG: String = FirstFragment::class.java.name

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        logd(mTAG, "${getToolBarHeight(requireActivity())}")

        addGuideView()
    }

    private fun addGuideView(){
        Guide.with(this).setGuideParameters(
            GuideParamter.Builder(this.requireContext())
//            .addHighLightView(textView)//直接加view
            .addHighLightViewById(R.id.textview_first)//添加id
            .setHighLightShap(Shape.RoundRect)
//            .setHighLightViewPadding(PaddingOffset(10, 10, 10, 10))
            .setRoundRectCorners(50F, 50F)
            .addTipContentViewById(R.layout.guide_layout)
//            .addTipContentView(tipView)//也可以直接添加view，因为这个页面可能多次使用
            .setTipViewOrientation(TipOrientation.BOTTOM)
            .setTipViewMargin(10)
            .setTipHintListener { loge(mTAG, "第一步") }
            .build()
                +
            GuideParamter.Builder(this.requireContext())
//            .addHighLightView(textView1)
            .addHighLightViewById(R.id.button_first)
            .setHighLightShap(Shape.RoundRect)
//            .setHighLightViewPadding(PaddingOffset(30, 15, 30, 15))
            .addTipContentViewById(R.layout.guide_layout)
//            .addTipContentView(tipView)//也可以直接添加view，因为这个页面可能多次使用
            .setTipViewOrientation(TipOrientation.BOTTOM)
            .setTipViewMargin(0)
            .setTipHintListener { loge(mTAG, "第二步") }
            .build()
        ).setLastStepListener {
            loge(mTAG, "最后一步")
        }.show()
    }
}