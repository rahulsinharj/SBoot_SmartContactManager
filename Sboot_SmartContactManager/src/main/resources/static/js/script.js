console.log("This is script message coming from backend side.")

const toggleSidebar = () => {
  if ($(".sidebar").is(":visible")) {
    //true
    //band karna hai
    $(".sidebar").css("display", "none");
    $(".content").css("margin-left", "0%");
  } else {
    //false
    //show karna hai
    $(".sidebar").css("display", "block");
    $(".content").css("margin-left", "20%");
  }
};

const contactSearch = ()=> {
  
	let query = $("#search-input").val();

	if(query=='')  {
    	$(".search-result").hide();
	}
	else
	{
	    console.log(query);
	
	    // Sending request to server::
	    let url = `http://localhost:8282/user/search/${query}`;
	    
	    fetch(url).then((response)=>{
	      return response.json();
	    }).then((data)=>{
	        console.log(data);
	
	        let text = `<div class='list-group'>`;
	
	        data.forEach((contact) => {
	          text += `<a href='/user/contact/${contact.cId}' class='list-group-item list-group-item-action'> ${contact.name}  </a>`;
	        });
	
	        text += `</div>`;
	
	        $(".search-result").html(text);
	        $(".search-result").show();
	    });

	}





}