package com.common.lib.net.moshi

//import com.common.lib.net.bean.Activity
//import com.common.lib.net.bean.ActivityGroup
//import com.common.lib.net.bean.ColumnContent
//import com.common.lib.net.bean.Essay
//import com.common.lib.net.bean.FitB
//import com.common.lib.net.bean.FreeResponse
//import com.common.lib.net.bean.Heading
//import com.common.lib.net.bean.KiraImage
//import com.common.lib.net.bean.MultipleChoice
//import com.common.lib.net.bean.Paragraph
//import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory

//object MoshiAdapters {
//    fun columnContentAdapter(): PolymorphicJsonAdapterFactory<ColumnContent> {
//        return PolymorphicJsonAdapterFactory.of(ColumnContent::class.java, "type")
//            .withSubtype(Heading::class.java, "heading")
//            .withSubtype(ActivityGroup::class.java, "activityGroup")
//            .withSubtype(Paragraph::class.java, "paragraph")
//            .withSubtype(KiraImage::class.java, "kiraImage")
//    }
//
//    fun activityAdapter(): PolymorphicJsonAdapterFactory<Activity> {
//        return PolymorphicJsonAdapterFactory.of(Activity::class.java, "type")
//            .withSubtype(FreeResponse::class.java, "freeResponse")
//            .withSubtype(Essay::class.java, "essay")
//            .withSubtype(MultipleChoice::class.java, "multipleChoice")
//            .withSubtype(FitB::class.java, "fitB")
//    }
//}
