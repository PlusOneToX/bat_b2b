<template>
	<div class="upload-img">
		<upload-file multiple :max-files="maxFiles" @post-url="getUrl" title="上传图片"></upload-file>
		<el-row v-if="url.length == 0">
			<div class="img-div">
				<img class= "img-default" src="/src/assets/images/add_img_default.png">
			</div>
		</el-row>
		<el-row class="box clearfix" v-if="url.length>0">
			<div class="left-side">
				<div class="show-img">
					<i class="el-icon-arrow-left arrow" @click="handleLeft"></i>
					<div class="img-content">
						<img :src="showImg" alt="">
						<div>
							<span>图片url:</span><span>{{showImg}}</span>
						</div>
					</div>
					<i class="el-icon-arrow-right arrow" @click="handleRight"></i>
				</div>
			</div>
			<div class="right-side">
				<div class="min-img">
					<div class="img-content" v-for="(item,index) in url" @click="handleClick(index)" :key="item.id">
						<img :src="item" alt="">
						<i class="el-icon-delete delete" @click="handleDelete(index)"></i>
					</div>
				</div>
			</div>
		</el-row>
	</div>
</template>
<script>
import uploadFile from "@/components/uploadFile"
export default {
	props: ['goods'],
	data() {
		return {
			index: 0,
			url: [],
		}
	},
	computed: {
		showImg() {
			return this.url[this.index]
		},
		maxFiles() {
			return 6 - this.url.length
		}
	},
	watch: {
		goods(val) {

			for(let i = 1; i < 7; i++) {
				let n = 'imageUrl' + i;
				if(val[n] != '') {
					this.url.push(val[n])
				}
			}
		}
	},
	components: { uploadFile },
	methods: {
		handleLeft() {
			this.index = this.index == 0 ? (this.url.length - 1) : (this.index - 1)
		},
		handleRight() {
			this.index = this.index == (this.url.length - 1) ? 0 : (this.index + 1)
		},
		handleDelete(index) {
			this.$confirm('删除此图片?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
			}).then(() => {
				this.url.splice(index, 1)
				if(this.index >= (this.url.length - 1)) {
					this.index = this.url.length - 1
				}
			})

		},
		getUrl(val) {
			if(val.length == 1) {
				this.url.push(val[0])
			} else {
				val.forEach(item => {
					this.url.forEach((v, i) => {
						if(v == item) {
							this.url.splice(i,1);
							return;
						}
					})
					this.url.push(item)
				})
			}

		},
		handleClick(index) {
			this.index = index;
		}
	}
}

</script>
<style lang="scss" scoped>
.upload-img {
	margin-top: 20px;
	.img-div {
		width: 600px;
		height: 600px;
		display: table-cell;
		vertical-align: middle;
		text-align: center;
		background-color: #bfbfbf;
		.img-default{
			width: 50px;
			height: 50px;
		}
	}
	.box {
		margin-top: 20px;
		height: 400px;
		padding: 10px;
		border: 2px solid #ccc;
	}
	.left-side {
		width: 68%;
		float: left;
		height: 100%;
		margin-right: 2%;
	}
	.left-side img {
		height: 345px;
	}
	.right-side {
		width: 30%;
		float: left;
		overflow: auto;
		height: 100%;
	}
	.arrow {
		font-size: 30px;
		position: absolute;
		opacity: 0.5;
		&:hover {
			opacity: 1;
			transform: scale(1.5);
		}
	}
	.show-img {
		position: relative;
	}
	.el-icon-arrow-left {
		left: 5%;
		top: 50%;
	}
	.el-icon-arrow-right {
		right: 5%;
		top: 50%;
	}
	.img-content {
		width: 100%;
		height: 100%;
		border: 1px solid #ccc;
		margin: 5px 0;
		text-align: center;
	}
	img {
		width: 90%;
		height: 100%;
	}
	.delete {
		position: relative;
		right: 10px;
		bottom: 10px;
		opacity: 0.5;
		z-index: 9999;
		color: blue;
		&:hover {
			opacity: 1;
			transform: scale(1.5);
		}
	}
}

</style>
