/**
 * 	动态获取limit值
 */
var show_items = function() {   		
			
	$("#type").change(function(){
		
        $("#limit").empty();
        
        var name = $("#type").val();
        
    	$.getJSON("/FLOP/category/findLimit.do", "name=" + name, function(resp){
    		var pro = resp.result;					    
    		$("#limit").val(resp);
    	})
	});
}

$(document).ready(function(){
	$.getJSON("/FLOP/category/findLimit.do", "name=writing", function(resp){
		$("#limit").val(resp);
	})
	show_items();
});