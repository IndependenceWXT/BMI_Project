function commitDate(){
	//1.获取身高和体重
	//document:当html文档加载完毕之后,会自动生成一个document对象
	//getElementById():
	//.value():获取标签的值
	var h=document.getElementById("h_num").value;
	var w=document.getElementById("w_num").value;
	if(h==""|| w==""){
		alert("不能为空！！");
	}else{
		//2.进行计算  bmi=体重[kg]/(身高^2)[m]
	console.log("h:"+h);
	console.log("w:"+w);
	var bmisum=w/((h/100)*(h/100));
	//Math.round(    )取整
	bmisum=Math.round(bmisum*100)/100;
	console.log("sum:"+bmisum);
	//建议体重：w=bmi*h*h[18.9~23.9];
	var wMin=18.9*(h/100)*(h/100);
	wMin=Math.round(wMin*100)/100;
	var wMax=23.9*(h/100)*(h/100);
	wMax=Math.round(wMax*100)/100;
	//3.显示到当前页面
	document.getElementById('bmi').innerHTML="您的bmi值："+bmisum;
	document.getElementById('w_lx').innerHTML="您的理想体重："+wMin+"~"+wMax;
	if(bmisum<=18.4){
		alert("您目前太瘦了！！");
	}else if(bmisum<23.9){
		alert("您目前处于完美的身材，请继续保持！！");
	}else if(bmisum<27.9){
		alert("您目前处于过重，多吃青菜，少吃肉！！");
	}else{
		alert("您目前处于肥胖，少吃零食！！");
	}
	//获取系统上的时间
	var date=new Date();
	var dateTime=date.getFullYear()+"-"+date.getMonth()+"-"+date.getDay();
	//date.getTime();//获取时间戳
	console.log(dateTime);
	//4.提交到后台
	$.ajax({
		type:"post",//请求方式
		url:"http://127.0.0.1:8080/bmiproject/servlet/BmiServlet?method=add_bmi",//请求地址 http://127.0.0.1:8080/项目名/url?method=add_bmi
		data:{//传递参数到后台，变量名:变量值，参数与参数之间逗号隔开
			height:h,
			weight:w,
			bmi:bmisum,
			datetime:dateTime
		},
		async:false,//是否同步
		timeout:5000,//设置超时时间
		dataType:"json",
		success:function(data){
			//data:后台打印回来的数据
			alert(data);
			//判断data是否为空
			if(!jQuery.isEmptyObject(data)){
				//更新的数据显示到页面
				var table2=document.getElementById("table2");
					//把随后一条数据，插入
					addBmiHistory(data[data.length-1]);	
			}else{
				alert("更新失败");
			}
		},
		error:function(xhr,textState){
			alert("请求失败！！");
		}
	});
	}
	
	
	
}
//当页面加载完毕之后，会执行此方法(相当于onload)
	$(document).ready(function(e){
		$.ajax({
			type:"post",
			url:"http://127.0.0.1:8080/bmiproject/servlet/BmiServlet?method=start_bmi",
			async:false,//是否同步
			timeout:5000,//设置超时时间
			dataType:"json",
			success:function(data){
				//alert(data);			
				var table2=document.getElementById("table2");
				for (var i in data) {
					addBmiHistory(data[i]);
				}
			},
			error:function(xhr,textState){
				alert("请求失败！！");
			}
		});
		
	});
function addBmiHistory(data){
	//通过document创建一个tr
	var tr1=document.createElement("tr");
	//给每个tr添加一个唯一的标识，便于删除
	tr1.id=data.id;
	//把创建的tr放table中

	table2.appendChild(tr1);
	tr1.innerHTML="<td>"+data.id+"</td><td>"+data.date+
	"</td><td>"+data.height+"</td><td>"+data.weight+
	"</td><td>"+data.bmi+"</td><td><a href='#' onclick='deleteData("+data.id+")'>删除</a></td>";
					
}
function deleteData(sign){
	$.ajax({
			type:"post",
			url:"http://127.0.0.1:8080/bmiproject/servlet/BmiServlet?method=delete_bmi",
			data:{
				sign:sign
			},
			async:false,
			timeout:5000,
			dataType:"text",
			success:function(data){
				//alert(data);	
				if(data==1||data=="1"){
					//根据tr的id,删除该行
					$("#"+sign).remove();
				}
				else{
					alert("很抱歉，删除失败！！");
				}
			},
			error:function(xhr,textState){
				alert("请求失败！！");
			}
		});
		
}
