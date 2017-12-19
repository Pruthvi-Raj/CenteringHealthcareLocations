<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
    <%@page import="chi.bean.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" language="javascript" src = "map.js"></script>
 <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<link rel = "stylesheet" type = "text/css" href = "map.css" />
<title>Locate Centering Sites</title>
<script type="text/javascript">
      google.charts.load('current', {
        'packages':['geochart'],
        // Note: you will need to get a mapsApiKey for your project.
        // See: https://developers.google.com/chart/interactive/docs/basic_load_libs#load-settings
        'mapsApiKey': 'AIzaSyBoizxb0FMB89793hKaM_fC1lX40vS3eJE'
      });
      google.charts.setOnLoadCallback(drawMarkersMap);
</script>
<script async defer
src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBoizxb0FMB89793hKaM_fC1lX40vS3eJE">
</script>


<% StateList stList = (StateList) request.getAttribute("StateList"); %>

</head>
<body>
 
 <input type="hidden" name="stateDetails" id="stateDetails"
		value="<%for (State s : stList.getStatesList()) {
				%>
				<%=s.getName()+", "+ s.getModName() +", "+ s.getTotalSites()+ "|"%>
			<%}%>" />

<form action="oauth" method="get" id="myform">

<div  align = "center" id="regions_div" ><a href=\"oauth\"></a></div>

<input type="hidden" style="margin-left:600px;margin-bottom:300px;" name="myField" id="myField" value="" />
<div id="tablecontainer">
 
</form>


</body>
</html>