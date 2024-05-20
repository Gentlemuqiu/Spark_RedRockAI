package com.example.redrockai.module.schoolroom.ui.fragment.Bean
import com.google.gson.annotations.SerializedName


data class RelatedCategoryBean(
    @SerializedName("adExist")
    val adExist: Boolean, // false
    @SerializedName("count")
    val count: Int, // 10
    @SerializedName("itemList")
    val itemList: List<Item>,
    @SerializedName("nextPageUrl")
    val nextPageUrl: String, // http://baobab.kaiyanapp.com/api/v1/tag/videos?start=10&num=10&strategy=date&id=12
    @SerializedName("total")
    val total: Int // 0
) {
    data class Item(
        @SerializedName("adIndex")
        val adIndex: Int, // -1
        @SerializedName("data")
        val `data`: Data,
        @SerializedName("id")
        val id: Int, // 223296
        @SerializedName("tag")
        val tag: Any, // null
        @SerializedName("trackingData")
        val trackingData: Any, // null
        @SerializedName("type")
        val type: String // followCard
    ) {
        data class Data(
            @SerializedName("adTrack")
            val adTrack: List<Any>,
            @SerializedName("content")
            val content: Content,
            @SerializedName("dataType")
            val dataType: String, // FollowCard
            @SerializedName("header")
            val header: Header
        ) {
            data class Content(
                @SerializedName("adIndex")
                val adIndex: Int, // -1
                @SerializedName("data")
                val `data`: Data,
                @SerializedName("id")
                val id: Int, // 0
                @SerializedName("tag")
                val tag: Any, // null
                @SerializedName("trackingData")
                val trackingData: Any, // null
                @SerializedName("type")
                val type: String // video
            ) {
                data class Data(
                    @SerializedName("ad")
                    val ad: Boolean, // false
                    @SerializedName("adTrack")
                    val adTrack: List<Any>,
                    @SerializedName("areaSet")
                    val areaSet: List<Any>,
                    @SerializedName("author")
                    val author: Author,
                    @SerializedName("brandWebsiteInfo")
                    val brandWebsiteInfo: Any, // null
                    @SerializedName("campaign")
                    val campaign: Any, // null
                    @SerializedName("candidateId")
                    val candidateId: Int, // 0
                    @SerializedName("category")
                    val category: String, // 剧情
                    @SerializedName("collected")
                    val collected: Boolean, // false
                    @SerializedName("consumption")
                    val consumption: Consumption,
                    @SerializedName("cover")
                    val cover: Cover,
                    @SerializedName("createTime")
                    val createTime: Long, // 1605761596000
                    @SerializedName("dataType")
                    val dataType: String, // VideoBeanForClient
                    @SerializedName("date")
                    val date: Long, // 1627748100000
                    @SerializedName("description")
                    val description: String, // 在一次毁灭性的分手之后，兰斯听了关于如何治愈他破碎的心的教学盒式磁带。From Chloé Aktas
                    @SerializedName("descriptionEditor")
                    val descriptionEditor: String, // 在一次毁灭性的分手之后，兰斯听了关于如何治愈他破碎的心的教学盒式磁带。From Chloé Aktas
                    @SerializedName("descriptionPgc")
                    val descriptionPgc: String,
                    @SerializedName("duration")
                    val duration: Int, // 552
                    @SerializedName("favoriteAdTrack")
                    val favoriteAdTrack: Any, // null
                    @SerializedName("id")
                    val id: Int, // 223296
                    @SerializedName("idx")
                    val idx: Int, // 0
                    @SerializedName("ifLimitVideo")
                    val ifLimitVideo: Boolean, // false
                    @SerializedName("infoStatus")
                    val infoStatus: String, // CONFIRMED
                    @SerializedName("label")
                    val label: Any, // null
                    @SerializedName("labelList")
                    val labelList: List<Any>,
                    @SerializedName("lastViewTime")
                    val lastViewTime: Any, // null
                    @SerializedName("library")
                    val library: String, // DEFAULT
                    @SerializedName("playInfo")
                    val playInfo: List<PlayInfo>,
                    @SerializedName("playUrl")
                    val playUrl: String, // http://baobab.kaiyanapp.com/api/v1/playUrl?vid=223296&resourceType=video&editionType=default&source=aliyun&playUrlType=url_oss&udid=
                    @SerializedName("played")
                    val played: Boolean, // false
                    @SerializedName("playlists")
                    val playlists: Any, // null
                    @SerializedName("premiereDate")
                    val premiereDate: Any, // null
                    @SerializedName("promotion")
                    val promotion: Any, // null
                    @SerializedName("provider")
                    val provider: Provider,
                    @SerializedName("reallyCollected")
                    val reallyCollected: Boolean, // false
                    @SerializedName("recall_source")
                    val recallSource: Any, // null
                    @SerializedName("releaseTime")
                    val releaseTime: Long, // 1627748100000
                    @SerializedName("remark")
                    val remark: String,
                    @SerializedName("resourceType")
                    val resourceType: String, // video
                    @SerializedName("searchWeight")
                    val searchWeight: Int, // 0
                    @SerializedName("shareAdTrack")
                    val shareAdTrack: Any, // null
                    @SerializedName("showLabel")
                    val showLabel: Boolean, // false
                    @SerializedName("slogan")
                    val slogan: String,
                    @SerializedName("sourceUrl")
                    val sourceUrl: String, // https://vimeo.com/479086114
                    @SerializedName("src")
                    val src: Any, // null
                    @SerializedName("status")
                    val status: String, // ONLINE
                    @SerializedName("subtitleStatus")
                    val subtitleStatus: String, // CRAWLER_NONE
                    @SerializedName("subtitles")
                    val subtitles: List<Any>,
                    @SerializedName("tags")
                    val tags: List<Tag>,
                    @SerializedName("tailTimePoint")
                    val tailTimePoint: Int, // 0
                    @SerializedName("thumbPlayUrl")
                    val thumbPlayUrl: String,
                    @SerializedName("title")
                    val title: String, // 如何治愈你破碎的心：走出失恋指南
                    @SerializedName("titlePgc")
                    val titlePgc: String, // 真正的危机来临，总统宣布世界末日
                    @SerializedName("translateStatus")
                    val translateStatus: String, // FINISH_TRANSLATION
                    @SerializedName("type")
                    val type: String, // NORMAL
                    @SerializedName("updateTime")
                    val updateTime: Long, // 1627748100000
                    @SerializedName("videoPosterBean")
                    val videoPosterBean: VideoPosterBean,
                    @SerializedName("waterMarks")
                    val waterMarks: Any, // null
                    @SerializedName("webAdTrack")
                    val webAdTrack: Any, // null
                    @SerializedName("webUrl")
                    val webUrl: WebUrl
                ) {
                    data class Author(
                        @SerializedName("adTrack")
                        val adTrack: Any, // null
                        @SerializedName("amplifiedLevelId")
                        val amplifiedLevelId: Int, // 5
                        @SerializedName("approvedNotReadyVideoCount")
                        val approvedNotReadyVideoCount: Int, // 0
                        @SerializedName("authorStatus")
                        val authorStatus: String, // ONLINE
                        @SerializedName("authorType")
                        val authorType: String, // MOCK
                        @SerializedName("cover")
                        val cover: String, // http://img.kaiyanapp.com/448bcc5e39bfaa7fa31aa1409cbd3247.png
                        @SerializedName("description")
                        val description: String, // 我们只做最好的。
                        @SerializedName("expert")
                        val expert: Boolean, // false
                        @SerializedName("follow")
                        val follow: Follow,
                        @SerializedName("icon")
                        val icon: String, // http://ali-img.kaiyanapp.com/50fe7ed6bbb5e5e52620e8664d58d323.png?imageMogr2/quality/60/format/jpg
                        @SerializedName("id")
                        val id: Int, // 2369
                        @SerializedName("ifPgc")
                        val ifPgc: Boolean, // true
                        @SerializedName("latestReleaseTime")
                        val latestReleaseTime: Long, // 1677342511000
                        @SerializedName("library")
                        val library: String, // DEFAULT
                        @SerializedName("link")
                        val link: String,
                        @SerializedName("name")
                        val name: String, // 剧情短片精选
                        @SerializedName("pendingVideoCount")
                        val pendingVideoCount: Int, // 0
                        @SerializedName("recSort")
                        val recSort: Int, // 0
                        @SerializedName("shield")
                        val shield: Shield,
                        @SerializedName("videoNum")
                        val videoNum: Int // 464
                    ) {
                        data class Follow(
                            @SerializedName("followed")
                            val followed: Boolean, // false
                            @SerializedName("itemId")
                            val itemId: Int, // 2369
                            @SerializedName("itemType")
                            val itemType: String // author
                        )

                        data class Shield(
                            @SerializedName("itemId")
                            val itemId: Int, // 2369
                            @SerializedName("itemType")
                            val itemType: String, // author
                            @SerializedName("shielded")
                            val shielded: Boolean // false
                        )
                    }

                    data class Consumption(
                        @SerializedName("collectionCount")
                        val collectionCount: Int, // 117
                        @SerializedName("playCount")
                        val playCount: Int, // 3002
                        @SerializedName("realCollectionCount")
                        val realCollectionCount: Int, // 105
                        @SerializedName("replyCount")
                        val replyCount: Int, // 4
                        @SerializedName("shareCount")
                        val shareCount: Int // 74
                    )

                    data class Cover(
                        @SerializedName("blurred")
                        val blurred: String, // http://ali-img.kaiyanapp.com/012ace13cbf8fdc5cebb4cb2a3645ad0.png?imageMogr2/quality/60/format/jpg
                        @SerializedName("detail")
                        val detail: String, // http://ali-img.kaiyanapp.com/1341bd69f7a3b5136f716af9fe787813.png?imageMogr2/quality/60/format/jpg
                        @SerializedName("feed")
                        val feed: String, // http://ali-img.kaiyanapp.com/1341bd69f7a3b5136f716af9fe787813.png?imageMogr2/quality/60/format/jpg
                        @SerializedName("homepage")
                        val homepage: String, // http://img.kaiyanapp.com/1341bd69f7a3b5136f716af9fe787813.png?imageView2/1/w/720/h/560/format/jpg/q/75|watermark/1/image/aHR0cDovL2ltZy5rYWl5YW5hcHAuY29tL2JsYWNrXzMwLnBuZw==/dissolve/100/gravity/Center/dx/0/dy/0|imageslim
                        @SerializedName("sharing")
                        val sharing: Any // null
                    )

                    data class PlayInfo(
                        @SerializedName("bitRate")
                        val bitRate: Int, // 523640
                        @SerializedName("dimension")
                        val dimension: String, // 1280x720
                        @SerializedName("duration")
                        val duration: Int, // 552
                        @SerializedName("height")
                        val height: Int, // 720
                        @SerializedName("id")
                        val id: Int, // 546003
                        @SerializedName("name")
                        val name: String, // 高清
                        @SerializedName("size")
                        val size: Int, // 36187083
                        @SerializedName("type")
                        val type: String, // high
                        @SerializedName("url")
                        val url: String, // http://baobab.kaiyanapp.com/api/v1/playUrl?vid=223296&resourceType=video&editionType=high&source=aliyun&playUrlType=url_oss&udid=
                        @SerializedName("urlList")
                        val urlList: List<Url>,
                        @SerializedName("videoId")
                        val videoId: Int, // 223296
                        @SerializedName("width")
                        val width: Int // 1280
                    ) {
                        data class Url(
                            @SerializedName("name")
                            val name: String, // aliyun
                            @SerializedName("size")
                            val size: Int, // 36187083
                            @SerializedName("url")
                            val url: String // http://baobab.kaiyanapp.com/api/v1/playUrl?vid=223296&resourceType=video&editionType=high&source=aliyun&playUrlType=url_oss&udid=
                        )
                    }

                    data class Provider(
                        @SerializedName("alias")
                        val alias: String, // vimeo
                        @SerializedName("icon")
                        val icon: String, // http://ali-img.kaiyanapp.com/c3ad630be461cbb081649c9e21d6cbe3.png
                        @SerializedName("id")
                        val id: Int, // 4
                        @SerializedName("name")
                        val name: String, // Vimeo
                        @SerializedName("status")
                        val status: String // SHOW
                    )

                    data class Tag(
                        @SerializedName("actionUrl")
                        val actionUrl: String, // eyepetizer://tag/1032/?title=%E7%BB%99%E4%BD%A0%E8%AE%B2%E4%B8%AA%E5%A5%BD%E6%95%85%E4%BA%8B
                        @SerializedName("adTrack")
                        val adTrack: Any, // null
                        @SerializedName("alias")
                        val alias: Any, // null
                        @SerializedName("bgPicture")
                        val bgPicture: String, // http://img.kaiyanapp.com/d471080a9de44e8fbaa4850887273332.jpeg?imageMogr2/quality/60/format/jpg
                        @SerializedName("childTagIdList")
                        val childTagIdList: Any, // null
                        @SerializedName("childTagList")
                        val childTagList: Any, // null
                        @SerializedName("communityIndex")
                        val communityIndex: Int, // 0
                        @SerializedName("desc")
                        val desc: String, // 每周末更新，关注听开眼给你讲故事。
                        @SerializedName("haveReward")
                        val haveReward: Boolean, // false
                        @SerializedName("headerImage")
                        val headerImage: String, // http://img.kaiyanapp.com/33a2b832b7583dd9781f9fd40ad7617e.jpeg?imageMogr2/quality/60/format/jpg
                        @SerializedName("id")
                        val id: Int, // 1032
                        @SerializedName("ifNewest")
                        val ifNewest: Boolean, // false
                        @SerializedName("ifShow")
                        val ifShow: Boolean, // false
                        @SerializedName("level")
                        val level: Int, // 0
                        @SerializedName("name")
                        val name: String, // 给你讲个好故事
                        @SerializedName("newestEndTime")
                        val newestEndTime: Any, // null
                        @SerializedName("parentId")
                        val parentId: Int, // 0
                        @SerializedName("recWeight")
                        val recWeight: Double, // 1.0
                        @SerializedName("relationType")
                        val relationType: Int, // 0
                        @SerializedName("tagRecType")
                        val tagRecType: String, // IMPORTANT
                        @SerializedName("tagStatus")
                        val tagStatus: String, // SHOW
                        @SerializedName("top")
                        val top: Int, // 0
                        @SerializedName("type")
                        val type: String // DEFAULT
                    )

                    data class VideoPosterBean(
                        @SerializedName("fileSizeStr")
                        val fileSizeStr: String, // 2.24MB
                        @SerializedName("scale")
                        val scale: Double, // 0.725
                        @SerializedName("url")
                        val url: String // http://eyepetizer-videos.oss-cn-beijing.aliyuncs.com/video_poster_share/d07c7ff7d03da51134f39f92cbb74070.mp4
                    )

                    data class WebUrl(
                        @SerializedName("forWeibo")
                        val forWeibo: String, // http://www.eyepetizer.net/detail.html?vid=223296&resourceType=video&utm_campaign=routine&utm_medium=share&utm_source=weibo&uid=0
                        @SerializedName("raw")
                        val raw: String // http://www.eyepetizer.net/detail.html?vid=223296
                    )
                }
            }

            data class Header(
                @SerializedName("actionUrl")
                val actionUrl: String, // eyepetizer://pgc/detail/2369/?title=%E5%89%A7%E6%83%85%E7%9F%AD%E7%89%87%E7%B2%BE%E9%80%89&userType=PGC&tabIndex=1
                @SerializedName("cover")
                val cover: Any, // null
                @SerializedName("description")
                val description: String, // #剧情
                @SerializedName("font")
                val font: Any, // null
                @SerializedName("icon")
                val icon: String, // http://ali-img.kaiyanapp.com/50fe7ed6bbb5e5e52620e8664d58d323.png?imageMogr2/quality/60/format/jpg
                @SerializedName("iconType")
                val iconType: String, // round
                @SerializedName("id")
                val id: Int, // 223296
                @SerializedName("label")
                val label: Any, // null
                @SerializedName("labelList")
                val labelList: Any, // null
                @SerializedName("rightText")
                val rightText: Any, // null
                @SerializedName("showHateVideo")
                val showHateVideo: Boolean, // false
                @SerializedName("subTitle")
                val subTitle: Any, // null
                @SerializedName("subTitleFont")
                val subTitleFont: Any, // null
                @SerializedName("textAlign")
                val textAlign: String, // left
                @SerializedName("time")
                val time: Long, // 1627748100000
                @SerializedName("title")
                val title: String // 剧情短片精选
            )
        }
    }
}