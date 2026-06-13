<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>

  <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
  <title>Output Screen</title>
	
  <script type="text/javascript" src="<c:url value="/webjars/jquery/3.7.1/jquery.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/webjars/bootstrap/5.3.3/js/bootstrap.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/resources/javascript/index.js"/>"></script>
  
  <link rel="stylesheet" href="<c:url value="/webjars/bootstrap/5.3.3/css/bootstrap.min.css"/>"/>  
  <link rel="stylesheet" href="<c:url value="/webjars/font-awesome/6.4.2/css/all.css"/>">
<style type="text/css">
  body{
     background: url('<c:url value="/resources/Images/bg.png"/>') no-repeat center center fixed;
	 background-size: cover;
	}
  	
	#captions_div {
	    background: #f9fafa; /* light neutral background */
	    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
	    font-family: 'Segoe UI', sans-serif;
	    color: #1a1a1a;
	    display: flex;
	    flex-wrap: wrap;
	    gap: 20px;
	    margin: 20px auto;
	}
	
	#captions_div label {
	    flex: 1 1 30%;
	    font-size: 1.4rem;
	    font-weight: 600;
	    color: #1a1a1a;
	}
	
	#captions_div label i {
	    margin-right: 8px;
	    color: #007bff;
	}
	
	/* Highlighted text like scores */
	#inning2_teamScore_lbl {
	    padding: 5px 10px;
	    border-radius: 10px;
	    display: inline-block;
	    font-weight: bold;
	}
	
	/* Green batsman name like "GUPTA*" */
	#inning1_battingcard1_lbl,
	#inning1_battingcard2_lbl {
	    color: #2e7d32;
	    font-weight: bold;
	}
	
  </style> 
  <script type="text/javascript">
	$(document).on("keydown", function(e){
		
		var evtobj = window.event? event:e;
		switch(e.target.tagName.toLowerCase()){
		case "input": case "textarea":
			break;
		default:
			if (e.which >= 112 && e.which <= 123) { // Suppress default behaviour of F1 to F12
				e.preventDefault();
			}
			processUserSelectionData('LOGGER_FORM_KEYPRESS',e.which);
		break;
		}
	});
	setInterval(() => {processCricketProcedures('READ-MATCH-AND-POPULATE');}, 1000);
  </script>
</head>
<body onload="onPageLoadEvent('OUTPUT')">
<body>
<form:form name="output_form" autocomplete="off" action="POST">
<div class="content py-5" style="background-color: #EAE8FF; color: #2E008B">
  <div class="container">
	<div class="row">
	<div class="col-md-14">	
       <span class="anchor"></span>
         <div class="card card-outline-secondary">
           <div class="card-header">
             <h3 class="mb-0">Output Trio</h3>
           </div>
          <div class="card-body">
			  <div id="select_graphic_options_div" style="display:none;">
			  </div>
			  <div id="main_captions_div" class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			  <div id="captions_div" class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label class="col-sm-4 col-form-label text-left">IP Address: ${session_selected_ip} </label>
			    <label class="col-sm-4 col-form-label text-left">Port Number: ${session_port} </label>
			    <label class="col-sm-4 col-form-label text-left">Broadcaster: ${session_selected_broadcaster} </label>
			     <label id = "selected_inning" class="col-sm-4 col-form-label text-left">Which Inning: ${which_keypress} </label>
			     <label class="col-sm-4 col-form-label text-left">Directory: ${mainCricketDir} </label>
				
				<label id="inning1_teamScore_lbl" class="col-sm-4 col-form-label text-left">-</label>			
				<label id="inning2_teamScore_lbl" class="col-sm-4 col-form-label text-left"> -</label>
				<label id="inning1_battingcard1_lbl" class="col-sm-4 col-form-label text-left">
				    <img id="batter1_img" src="<c:url value="/resources/Images/batter.png" />" alt="Batter" style="width:50px; height:50px; vertical-align:middle; margin-right:5px;">
				    <span id="batter1_text">-</span>
				</label>
				
				<label id="inning1_battingcard2_lbl" class="col-sm-4 col-form-label text-left">
				    <img id="batter2_img" src="<c:url value="/resources/Images/batter.png" />" alt="Batter" style="width:50px; height:50px; vertical-align:middle; margin-right:5px;">
				    <span id="batter2_text">-</span>
				</label>
				
				<label id="inning1_bowlingcard_lbl" class="col-sm-4 col-form-label text-left">
				    <img id="bowler_img" src="<c:url value="/resources/Images/bowler.png" />" alt="Bowler" style="width:50px; height:50px; vertical-align:middle; margin-right:5px;">
				    <span id="bowler_text">-</span>
				</label>
			  </div>
			</div>
	      </div>
	    </div>
       </div>
    </div>
    <div class="row mt-4" id="caption_working_section">
      <div class="col-md-10 offset-md-1">
        <div class="card card-outline-secondary">
          <div class="card-header">
            <h3 class="mb-0" style="font-size: clamp(1rem, 2.5vw, 1.6rem); letter-spacing: 1px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">Caption &amp; Working Press(1 for first inning and 2 for second inning)</h3>
          </div>
          <div class="card-body p-0">
            <div class="table-responsive">
              <table class="table table-bordered table-hover mb-0" id="caption_working_table">
                <thead style="background-color: #2E008B; color: #ffffff;">
                  <tr>
                    <th scope="col" style="width: 5%;">#</th>
                    <th scope="col" style="width: 30%;">Caption</th>
                    <th scope="col" style="width: 35%;">Working</th>
                  </tr>
                </thead>
                <tbody id="caption_working_tbody">
                  <!-- Row 1 -->
                  <tr>
                    <td>1</td><td>(b)</td><td>Comparison</td>
                  </tr>
                  <!-- Row 2 -->
                  <tr>
                    <td>2</td><td>(c)</td><td>Equation</td>
                  </tr>
                  <!-- Row 3 -->
                  <tr>
                    <td>3</td><td>(a)</td><td>Projected</td>
                  </tr>
                 <!-- Row 4 -->
                  <tr>
                    <td>4</td><td>(f5)</td><td>Target</td>
                  </tr> 
                  <!-- Row 5 -->
                 <!--  <tr>
                    <td>5</td><td>(n)</td><td>Opener name</td>
                  </tr> -->
                  <!-- Row 6 -->
                  <!-- <tr>
                    <td>6</td><td>(F8)</td><td>Profile</td>
                  </tr> -->
                  <!-- Row 6 -->  
                 <!-- <tr>
                    <td>7</td><td>(F2)</td><td>In At</td>
                  </tr>  -->
                   <!-- Row 8 -->  
                 <tr>
                    <td>5</td><td>(F1)</td><td>CRR & RRR for inning 2</td>
                  </tr> 
                  <!-- Row 9 -->  
                 <tr>
                    <td>6</td><td>(F3)</td><td>Boundaries</td>
                  </tr> 
                  <!-- Row 10 -->  
                 <tr>
                    <td>7</td><td>(t)</td><td>Toss</td>
                  </tr> 
                  <!-- Row 11-->  
                 <tr>
                   <td>8</td><td>(F7)</td><td>Next to bat</td>
                  </tr> 
                  <!-- Row 12-->  
                 <tr>
                   <td>9</td><td>(v)</td><td>Result/Victory gfx</td>
                  </tr> 
                  <tr>
                   <td>10</td><td>(x)</td><td>Last x overs</td>
                  </tr>  
                 <tr>
                   <td>11</td><td>(y)</td><td>Last x Balls</td>
                  </tr> 
                   <tr>
                   <td>12</td><td>( j )</td><td>Leaderboard Most Runs</td>
                  </tr> 
                   <tr>
                   <td>13</td><td>( w )</td><td>Leaderboard Most Wickets</td>
                  </tr>     
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<input type="hidden" name="select_broadcaster" id="select_broadcaster" value="${session_selected_broadcaster}"/>
<input type="hidden" id="which_keypress" name="which_keypress" value="${which_keypress}"/>
</form:form>
</body>
</html>