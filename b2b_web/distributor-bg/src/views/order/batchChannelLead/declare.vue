<template>
	<div class="declare-lead">
		<header>
			<h4>填写说明</h4>
			<el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="cancel">
				返回导入订单
			</el-button>
		</header>

		<div class="declare-header">
			<div>
				<h4 style="margin-bottom: 15px;">请仔细阅读CSV模版中以下信息的填写方式。</h4>
				<br/>
				<h4>注意：凡是表示 "*" 的字段都为必填项，不带 "*" 的可不填。</h4>
			</div>
		</div>

		<div class="declare-body">
			<div class="declare-one border">
				<h4>导入订单说明</h4>
			</div>
			<div class="declare-step">
				<div style="margin-bottom: 15px;">
					<h4>1、Excel文件中一张工作表为一个订单，可以在一个Excel文件中添加多张工作表，从而批量导入多张订单。</h4>
				</div>
				<div>
					<h4>2、订单导入可能出现因为部分名称无法识别导致字段获取失败的情况，业务员需要在提交订单前将必填信息重新补充完整。</h4>
				</div>
			</div>
			<div class="declare-one">
				<h4>字段说明</h4>
			</div>
			<div>
				<table cellspacing="0" cellpadding="0" style="width: 100%;">
					<tr>
						<td style="padding: 10px;"><h4>B2B编码</h4></td>
						<td>
							<h4 style="margin-top: 15px;">在“B2B编码”下一行填入下单客户的“B2B编码”。可在“合作中的分销商列表”中，查看分销商的资料中可查询到。必填。</h4>
							<div class="bg2_td"></div>
						</td>
					</tr>
					<tr>
						<td style="padding: 10px;"><h4>分销商ID</h4></td>
						<td>
							<h4 style="margin-top: 15px;">在“分销商ID”下一行填入下单客户的“分销商ID”。可在“合作中的分销商列表”中，查看分销商的资料中可查询到。</h4>
							<div class="bg_td"></div>
						</td>
					</tr>
					<tr>
						<td style="padding: 15px;"><h4>配送方式</h4></td>
						<td><h4>配送方式只能填写目前后台已配置的配送方式名称</h4></td>
					</tr>
					<tr>
						<td style="padding: 15px;"><h4>支付方式</h4></td>
						<td><h4>后台导入订单的支付方式只支持“区间结算”和“公司转账”</h4></td>
					</tr>
					<tr>
						<td style="padding: 15px;"><h4>订单类型</h4></td>
						<td><h4>订单类型只能填写目前后台已配置的“订单类型”，主要包括直运销售订单、寄售销售订单、标准销售订单、租赁销售订单、受托加工销售等。</h4></td>
					</tr>
					<tr>
						<td style="padding: 15px;"><h4>存货编码</h4></td>
						<td><h4>存货编码应填写货品的80码。</h4></td>
					</tr>
				</table>
			</div>
		</div>



	</div>
</template>

<script type="text/javascript">
	export default {
		name: 'declare',
		data() {
			return {

			}
		},
		methods: {
			cancel() { // 返回操作
				this.$router.push({ name: 'batchChannelLead'})
			}
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.bg_td {
		background-image: url('../batchChannelLead/import.png');
		background-position:  center;
		background-repeat:no-repeat;
		background-position: 20px 20px;
		width: 810px;
		height: 500px;
	}
	.bg2_td {
		background-image: url('../batchChannelLead/b2bimport.png');
		background-position:  center;
		background-repeat:no-repeat;
		background-position: 20px 20px;
		width: 810px;
		height: 500px;
	}
	table {
		border-right: 1px solid #dcdcdc;
		border-bottom: 1px solid #dcdcdc;
		
	}
	table td {
		border-left: 1px solid #dcdcdc;
		border-top: 1px solid #dcdcdc;
		word-break: keep-all;
	}
	.declare-lead {
		background-color: white;
		min-width: 1050px;
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
		.btn-home{
			float: right;
			padding: 5px;
			margin-top: 8px;
			margin-right: 8px;
			margin-left: 0;
			font-size: 12px;
		}
		.declare-header {
			width: 95%;
			margin: 50px auto;
			background-color: #f7f7f7;
			border: 1px solid #dcdcdc;
			border-radius: 6px;
			padding: 15px;
		}
		.declare-body {
			margin: 50px auto;
			width: 95%;
			border-radius: 6px;
			.declare-one {
				padding: 15px;
				border: 1px solid #dcdcdc;
				background-color: #f7f7f7;
				.tr {
					.td {
						padding: 10px;
					}
				}
			}
			.declare-one.border {
				border-top-left-radius: 6px;
				border-top-right-radius: 6px;
			}
			.declare-step {
				padding: 17px;
				border-left: 1px solid #dcdcdc;
				border-right: 1px solid #dcdcdc;
			}
		}
	}
</style>
