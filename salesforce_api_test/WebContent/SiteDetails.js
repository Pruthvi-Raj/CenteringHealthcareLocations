/**
 * 
 */

function drawMap(){
	var d = document.getElementById('siteAddress1').value
	console.log(d)
	var array = d.split('|');
	//console.log(array[20])
	var i;
	
	
	var dataArray = [['siteName']];
	
	for (i=0;i<array.length-1;i++) {
		console.log("Javascript"+array[i])
		dataArray.push([array[i]])
	}
	
	console.log(dataArray)
	
	
	var data = google.visualization.arrayToDataTable(dataArray);
	
	var state = document.getElementById('stateValue').value;
	var newState = "US-"+state;
	//alert(newState)
	var options = {
			region: newState,
			 backgroundColor: '#eee',
			    datalessRegionColor: '#179ADF',
			    width: 1000,
			    height: 800,
			    zoon:12,
			    displayMode: 'markers',
			    resolution: 'provinces',
			    defaultColor: '#EEEE51',
			    //legend: 'none',
			    magnifyingGlass: { zoomFactor: 10.0},
			    colorAxis: {colors: ['#e7711c', '#4374e0']},
			    enableRegionInteractivity: 'true',
	};
	
	 var container = document.getElementById('mapcontainer');
	  var chart = new google.visualization.GeoChart(container,{
		  zoom: 12
	  });
	  
	  
	  google.visualization.events.addListener(chart, 'select', myClickHandler);
      
      var lastEvent = null;
      container.addEventListener('click', function (e) {
        lastEvent = e;
      }, false);
      /*container.addEventListener('mouseover', function (e) {
        lastEvent = e;
        // dispatch click event to get hover value
        var event = document.createEvent('SVGEvents');
        event.initEvent('click', true, true);
        e.target.dispatchEvent(event);
      }, false);
*/
	
	//console.log(siteArray)
	
      
      
      var a
      for(a=0;a<siteList.count;a++){
    	  console.log(siteList[a])
      }
      
      console.log("siteList "+siteList)
      
      function myClickHandler() {
    	    var selection = chart.getSelection();
    	    var message = '';
    	    if (selection.length > 0) {
    	      if (selection[0].row !== null) {
    	          // click
    	          console.log("/" + data.getValue(selection[0].row, 0));
    	          var selectedSiteName = data.getValue(selection[0].row, 0);
      	        //alert('Selected site is: ' + selectedSiteName);
      	      console.log("next for loop")


      	    var siteList = document.getElementById('siteAddress1').value   
            var siteListArray = siteList.split('|')
      	      
      	    var siteUrlList = document.getElementById('siteUrl').value
      	    var siteUrlListArray = siteUrlList.split('|')
      	
		      	for (i=0;i<siteListArray.length-1;i++) {
		      		//console.log("Site Names"+siteListArray[i])
		      		//console.log(i)
		      		var mySite = siteListArray[i]
		      		var myUrl = siteUrlListArray[i]
		      		
		      		console.log("Current site "+ mySite + "\n"+"Selected site "+ selectedSiteName)
		      		if(selectedSiteName == mySite){
		      			console.log("Url of the page is "+ myUrl )
		      			console.log(i)
		      			window.location.href = myUrl;
		      			break
		      		}
		      	}
      	
      	        
    	      }
    	    }
    	  }
      
	  chart.draw(data, options);
}


google.charts.load('current', {
	  packages:['geochart'],
	  callback: drawMap
	});


function myFunction() {
    var input, filter, ul, li, a, i;
    input = document.getElementById("searchField");
    filter = input.value.toUpperCase();
    ul = document.getElementById("regions");
    li = ul.getElementsByTagName("li");
    //model = li.getElementById("modelDiv");
    for (i = 0; i < li.length; i++) {
        a = li[i].getElementsByTagName("a")[0];
        modelDiv = li[i].getElementsByTagName("b")[0];
        //model = modelDiv.getElementsByTagname("b");
        if (a.innerHTML.toUpperCase().indexOf(filter) > -1 || modelDiv.innerHTML.toUpperCase().indexOf(filter) > -1 ) {
            li[i].style.display = "";
        } else {
            li[i].style.display = "none";

        }
    }
}












