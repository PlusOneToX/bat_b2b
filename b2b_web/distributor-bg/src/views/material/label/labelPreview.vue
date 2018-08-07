<template>
	<div class="add-productline" v-loading="loading" >
		 <header style="margin-bottom: 0px;">
		  <h4>标签预览</h4>
		  <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="handleCancel">
			返回
		  </el-button>
		</header>
		<div  style="text-align: center;height: 100%;width: 100%; margin-top: 50px;">
			<canvas id="canvas" ></canvas>
		</div>
	</div>
</template>
<script>
import PDFJS from 'pdfjs-dist'
export default {
	data() {
		return {
			templateFile:'',
			pdfDoc:'',
			loading:false,
		}
	},
	created() {
		if(this.$route.params.labelId !== undefined){
			this.templateFile ='admin/u/p/rx/label/preview/'+this.$route.params.labelId
			this.loadFile(this.templateFile)
		}
		if(this.$route.params.url !== undefined){
			this.loadFile(this.$route.params.url)
		}
	},
	activated(){
		
	},
	methods: {
		renderPage () {
			this.pdfDoc.getPage(1).then((page) => {
				let canvas = document.getElementById('canvas')
				let ctx = canvas.getContext('2d')
				let dpr = window.devicePixelRatio || 1
				let bsr = ctx.webkitBackingStorePixelRatio ||
					ctx.mozBackingStorePixelRatio ||
					ctx.msBackingStorePixelRatio ||
					ctx.oBackingStorePixelRatio ||
					ctx.backingStorePixelRatio || 1
				let ratio = dpr / bsr
				let viewport = page.getViewport(screen.availWidth / page.getViewport(1).width)
				canvas.width = viewport.width * ratio
				canvas.height = viewport.height * ratio
				canvas.style.width = 745 + 'px'
				canvas.style.height = 435 + 'px'
				// canvas.style.width = canvas.width
       			// canvas.style.height = canvas.height
				ctx.setTransform(ratio, 0, 0, ratio, 0, 0)
				let renderContext = {
					canvasContext: ctx,
					viewport: viewport
				}
				page.render(renderContext)
			})
		},
		loadFile (url) {
			this.loading = true
			// this.$http.tabPreview(this, {id: url}).then(res => {
			this.$api.getData(this,url).then(res =>{		
				this.loading = false
				if(res === undefined || res.success == undefined) {
					this.timeData = [] //..成功下载Excel后初始化选中时间
					// this.formData.status = [] //..成功夏侯Excel后初始化订单状态
				}
				const content = res
				PDFJS.getDocument(content).then((pdf) => {
					this.pdfDoc = pdf
					this.$nextTick(() => {
						this.renderPage()
					})
				})
			})
		},
		handleCancel() {  // 返回操作
			this.$router.go(-1)
		},
	},
}

</script>
<style lang="scss" scoped>
.add-productline {
	background-color: #5B5B5B;
	height: 878px;
	width: 100%;
	// position: absolute;
	.header-row {
		@include table-header-color;
	}
	header {
		color: white;
		height: $lineHight;
		line-height: $lineHight;
		background-color: $lakeBlue;
		
	}
	h4 {
		display: inline-block;
		font-weight: 350;
		font-size: 16px;
		margin: 0 20px;
	}
}

</style>
