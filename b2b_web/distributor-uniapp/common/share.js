export default{
    data(){
        return {
            //设置默认的分享参数
            share:{
                title:'BAT官方商城',
                path:'/pages/index/index',
                imageUrl:'',
                desc:'BAT官方商城',
                content:'终端商户微信小程序自助下单、分销商城'
            }
        }
    },
    onShareAppMessage(res) {
		console.log('fenxiang---:',res);
		let title=this.share.title;
		let imageUrl=this.share.imageUrl;
		let path=this.share.path;
		if(res.from=='button'){
			console.log(res.target.dataset.goodsName);
			 let student=res.target.dataset.student;
			 title=student.goodsName;
			 imageUrl=student.imageUrl1;
			 path='/pages/classify/goodsDetails?id='+student.id
		}
        return {
            title:title,
            path:path,
            imageUrl:imageUrl,
            desc:this.share.desc,
            content:this.share.content,
            success(res){
                uni.showToast({
                    title:'分享成功'
                })
            },
            fail(res){
                uni.showToast({
                    title:'分享失败',
                    icon:'none'
                })
            }
        }
    },
	onShareTimeline(res){
		console.log('fenxiang---:',res);
		let title=this.share.title;
		let imageUrl=this.share.imageUrl;
		if(res.from=='button'){
			console.log(res.target.dataset.goodsName);
			 let student=res.target.dataset.student;
			 title=student.goodsName;
			 imageUrl=student.imageUrl1;
		}
		return {
		    title:title,
		    path:this.share.path,
		    imageUrl:imageUrl,
		    desc:this.share.desc,
		    content:this.share.content,
		    success(res){
		        uni.showToast({
		            title:'分享成功'
		        })
		    },
		    fail(res){
		        uni.showToast({
		            title:'分享失败',
		            icon:'none'
		        })
		    }
		}
	}
}