/**
 * 
 */
function drawMarkersMap() {

	var d = document.getElementById('stateDetails').value
	var array = d.split('|');
	var dataArray = [ [ 'City', 'CenteringPregnancy', 'CenteringParenting' ] ];
	var newDataArray = [ [ 'stateName', 'Totalsites' ] ];

	for (i = 0; i < array.length - 1; i++) {
		var array1 = array[i].split(',');
		var subArray = []
		dataArray.push(array1)
	}

	//console.log("HERE WE ARE DON'T TURN AWAY NOW, WE ARE THE WARRIORS THAT BUILT THIS TOWN \n"+ dataArray)
	for (i = 1; i < dataArray.length - 1; i++) {

		if (dataArray[i][0] == dataArray[i + 1][0]) {

			if (dataArray[i][1] == "CenteringPregnancy") {
				dataArray[i][1] = dataArray[i][2]
			}
			if (dataArray[i + 1][1] == "CenteringPregnancy") {
				dataArray[i][1] = dataArray[i + 1][2]
			}
			if (dataArray[i][1] == "CenteringParenting") {
				dataArray[i][2] = dataArray[i][2]
			}
			if (dataArray[i + 1][1] == "CenteringParenting") {
				dataArray[i][2] == dataArray[i + 1][2]
			}
			dataArray[i][1] = dataArray[i][1] + "," + dataArray[i + 1][1]
			dataArray[i][2] = dataArray[i][2] + "," + dataArray[i + 1][2]

			dataArray.splice(i + 1, 1)
		}
		if (dataArray[i][0] == dataArray[i - 1][0]) {

			if (dataArray[i][1] == "CenteringPregnancy") {
				dataArray[i][1] = dataArray[i][2]
			}
			if (dataArray[i - 1][1] == "CenteringPregnancy") {
				dataArray[i][1] = dataArray[i - 1][2]
			}
			if (dataArray[i][1] == "CenteringParenting") {
				dataArray[i][2] = dataArray[i][2]
			}
			if (dataArray[i - 1][1] == "CenteringParenting") {
				dataArray[i][2] == dataArray[i - 1][2]
			}

			dataArray[i - 1][1] = dataArray[i][1] + "," + dataArray[i - 1][1]
			dataArray[i - 1][2] = dataArray[i][2] + "," + dataArray[i - 1][2]

			dataArray.splice(i, 1)

		}

	}

	for (i = 1; i < dataArray.length; i++) {
		var x = dataArray[i][2]
		var newX = x.split(',')
		var sumX = 0
		for (j = 0; j < newX.length; j++) {
			sumX = sumX + parseInt(newX[j])
		}
		console.log(dataArray[i][0])
		console.log(sumX)
		var stateName = dataArray[i][0]
		console.log(stateName.trim())
		var res = stateName.trim()
		newDataArray[i] = [ res, sumX ]
		console.log(newDataArray[i])
	}

	console.log(dataArray)
	console.log(newDataArray)

	var data = google.visualization.arrayToDataTable(newDataArray);
	var options = {
		width : 1000,
		height : 700,
		datalessRegionColor : '#179ADF',
		legend : 'none',
		colorAxis : {
			colors : [ '#179ADF', '#179ADF' ]
		},
		region : 'US',
		enableRegionInteractivity : 'true',
		//displayMode: 'markers',
		resolution : "provinces"
	};

	var container = document.getElementById('regions_div');
	var chart = new google.visualization.GeoChart(container);

	google.visualization.events.addListener(chart, 'select', myClickHandler);

	var lastEvent = null;
	container.addEventListener('click', function(e) {
		lastEvent = e;
	}, false);

	function myClickHandler() {
		var selection = chart.getSelection();
		var message = '';
		if (selection.length > 0) {
			if (selection[0].row !== null) {
				// click
				console.log("/" + data.getValue(selection[0].row, 0));
				var value1 = data.getValue(selection[0].row, 0);
				//alert('City is: ' + value1);
				document.getElementById('myField').value = value1;
				document.getElementById("myform").submit();
			}
		}
	}

	chart.draw(data, options);

};
