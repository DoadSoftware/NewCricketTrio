package com.cricket.broadcaster;

import java.io.File;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.eclipse.tags.shaded.org.apache.xalan.xsltc.compiler.sym;

import com.cricket.config.DatabaseContextHolder;
import com.cricket.containers.Scene;
import com.cricket.controller.IndexController;
import com.cricket.ispl.mvp_leaderBoard;
import com.cricket.model.BattingCard;
import com.cricket.model.BestStats;
import com.cricket.model.BowlingCard;
import com.cricket.model.Event;
import com.cricket.model.EventFile;
import com.cricket.model.Fixture;
import com.cricket.model.HeadToHead;
import com.cricket.model.Inning;
import com.cricket.model.Match;
import com.cricket.model.MatchAllData;
import com.cricket.model.OverByOverData;
import com.cricket.model.Player;
import com.cricket.model.Setup;
import com.cricket.model.Statistics;
import com.cricket.model.StatsType;
import com.cricket.model.Team;
import com.cricket.model.Tournament;
import com.cricket.service.CricketService;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class DOAD_TRIO extends Scene{
	
	public String mainCricketDirectory = CricketUtil.CRICKET_DIRECTORY; 
	public String secondCricketDirectory = CricketUtil.CRICKET2_DIRECTORY;
	public char return_key = (char) 13;
	public char line_feed = (char) 10;
	public Inning inning;
	public List<Fixture> FixturesList = new ArrayList<Fixture>();
	public Team team;
	public Player player;
	public Player player2;
	public mvp_leaderBoard mvp;
	public StatsType statsType;
	public StatsType statsType2;
	public List<StatsType> statsTypes;
	public List<StatsType> statsTypes2;
	public Statistics stat;
	public Statistics stat2;
	public List<Statistics> statistics;
	public List<Statistics> statistics2;
	public List<Tournament> PastDataToCrr;
	public List<Tournament> PastDataToCrr2;
	public List<BestStats> top_Batsman,topbowler= new ArrayList<BestStats>();
	public List<BestStats> top_Batsman2,topbowler2= new ArrayList<BestStats>();
	public List<Tournament> tournament_stats = new ArrayList<Tournament>();
	public Tournament tournament;
	public Tournament tournament2;
	public List<String> this_data_str = new ArrayList<String>();
	
	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
		    .connectTimeout(Duration.ofSeconds(5)).build();
	
	double average = 0 ,average2 =0;
	String Data = "",hundred = "",fifty = "",strikeRate = "", thirty = "",
		batAverage = "",economy = "",best = "",runs = "",short_name = "", surName = "",profile ="",debut1 = "",debut2 = "" ,debut = "",debut3 ="";
	String Data2 = "",strikeRate2 = "", runs2 = "",  best2 = "",whichprofile,
			batAverage2 = "",pageName = "";
	
	public DOAD_TRIO() {
		super();
	}
	
	public DOAD_TRIO(String mainCricketDirectory) {
		super();
		this.mainCricketDirectory = mainCricketDirectory;
	}

	public Object ProcessGraphicOption(String whatToProcess, CricketService cricketService, MatchAllData match, PrintWriter print_writer, 
		HeadToHead headToHead, List<Tournament> pasttornament,List<BestStats> tapeball, String valueToProcess, String category,String ipAddress,String showname) throws Exception
	{
		switch(whatToProcess.toUpperCase()) {
		//Load Scene
		case "LOAD_GRAPHICS":
			DoadWriteToTrio(print_writer, "read_template 9020-Team_Single");
			break;
		//Save Scene	
		case "SAVE_GRAPHICS":
			DoadWriteToTrio(print_writer, "saveas " + valueToProcess);
			break;
			
		case "POPULATE_GRAPHICS_ISPL_50_50":
			if(valueToProcess.split(",")[0].equalsIgnoreCase("drone")) {
				DoadWriteToTrio(print_writer, "read_template 50-50-OVERS-PlayerImage_New_drone");
			}else {
				DoadWriteToTrio(print_writer, "read_template 50-50-OVERS-PlayerImage_New");
			}
			popualteIspl50_50(print_writer, Integer.valueOf(valueToProcess.split(",")[1]), valueToProcess.split(",")[2],match, cricketService);
			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[3]);
			break;
//		case "POPULATE_GRAPHICS_ISPL_FF_MATCH_SUMMARY": case "POPULATE_ISPL_FF_MATCH_SUMMARY":
//			DoadWriteToTrio(print_writer, "read_template FF_Match_Summary");
//			MatchAllData previous_match = new MatchAllData();
//			if(whatToProcess.equalsIgnoreCase("POPULATE_GRAPHICS_ISPL_FF_MATCH_SUMMARY")) {
//				Fixture fixture = cricketService.getFixtures().stream().filter(fix -> fix.getMatchnumber() == Integer.valueOf(valueToProcess.split(",")[0])).findAny().orElse(null);
//				if(new File(mainCricketDirectory + CricketUtil.SETUP_DIRECTORY + fixture.getMatchfilename() + ".json").exists()) {
//					previous_match.setSetup(new ObjectMapper().readValue(new File(mainCricketDirectory + CricketUtil.SETUP_DIRECTORY + 
//							fixture.getMatchfilename() + ".json"), Setup.class));
//					previous_match.setMatch(new ObjectMapper().readValue(new File(mainCricketDirectory + CricketUtil.MATCHES_DIRECTORY + 
//							fixture.getMatchfilename() + ".json"), Match.class));
//				}
//				if(new File(mainCricketDirectory + CricketUtil.EVENT_DIRECTORY + fixture.getMatchfilename() + ".json").exists()) {
//					previous_match.setEventFile(new ObjectMapper().readValue(new File(mainCricketDirectory + CricketUtil.EVENT_DIRECTORY + 
//							fixture.getMatchfilename() + ".json"), EventFile.class));
//				}
//				
////				previous_match = CricketFunctions.populateMatchVariables(cricketService, CricketFunctions.readOrSaveMatchFile(CricketUtil.READ, 
////						CricketUtil.SETUP + "," + CricketUtil.MATCH, previous_match,false));
//			}else {
//				previous_match = match;
//			}
//			popualteIspl_MATCH_SUMMARY(print_writer,previous_match, cricketService);
//			DoadWriteToTrio(print_writer, "saveas " + (valueToProcess.split(",").length >1 ? valueToProcess.split(",")[1] :valueToProcess.split(",")[0]));
//			break;
		case "POPULATE_GRAPHICS_ISPL_FF_MATCH_SUMMARY":
			DoadWriteToTrio(print_writer, "read_template FF_MATCH_SUMMARY");
			MatchAllData previous_match = new MatchAllData();
			if(whatToProcess.equalsIgnoreCase("POPULATE_GRAPHICS_ISPL_FF_MATCH_SUMMARY")) {
				Fixture fixture = cricketService.getFixtures().stream().filter(fix -> fix.getMatchnumber() == Integer.valueOf(valueToProcess.split(",")[0])).findAny().orElse(null);
				if(new File(mainCricketDirectory + CricketUtil.SETUP_DIRECTORY + fixture.getMatchfilename() + ".json").exists()) {
					previous_match.setSetup(new ObjectMapper().readValue(new File(mainCricketDirectory + CricketUtil.SETUP_DIRECTORY + 
							fixture.getMatchfilename() + ".json"), Setup.class));
					previous_match.setMatch(new ObjectMapper().readValue(new File(mainCricketDirectory + CricketUtil.MATCHES_DIRECTORY + 
							fixture.getMatchfilename() + ".json"), Match.class));
				}
				if(new File(mainCricketDirectory + CricketUtil.EVENT_DIRECTORY + fixture.getMatchfilename() + ".json").exists()) {
					previous_match.setEventFile(new ObjectMapper().readValue(new File(mainCricketDirectory + CricketUtil.EVENT_DIRECTORY + 
							fixture.getMatchfilename() + ".json"), EventFile.class));
				}
				
//				previous_match = CricketFunctions.populateMatchVariables(cricketService, CricketFunctions.readOrSaveMatchFile(CricketUtil.READ, 
//						CricketUtil.SETUP + "," + CricketUtil.MATCH, previous_match,false));
			}else {
				previous_match = match;
			}
			popualteIspl_MATCH_SUMMARY(print_writer,previous_match, cricketService);
			DoadWriteToTrio(print_writer, "saveas " + (valueToProcess.split(",").length >1 ? valueToProcess.split(",")[1] :valueToProcess.split(",")[0]));
			break;
		case "POPULATE_GRAPHICS_ISPL_TAPE":
			if(valueToProcess.split(",")[0].equalsIgnoreCase("drone")) {
				DoadWriteToTrio(print_writer, "read_template TAPE-BALL-OVERS_PLAYER_DRONE");
			}else {
				DoadWriteToTrio(print_writer, "read_template TAPE-BALL-OVERS_PLAYER");
			}
			popualteIsplTape(print_writer, Integer.valueOf(valueToProcess.split(",")[1]),match, cricketService);
			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[2]);
			break;
			
//		case"POPULATE_GRAPHICS_COMPARISION":
//			if(valueToProcess.split(",")[1].equalsIgnoreCase("AR")){
//				DoadWriteToTrio(print_writer, "read_template Comparison");
//			}else {
//				DoadWriteToTrio(print_writer, "read_template Comparison-Drone");
//			}
//			
//			populateComparison(print_writer, match,category);
//			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[0]);
//			break;
//		case "POPULATE_GRAPHICS_LASTXOVERS":
//			DoadWriteToTrio(print_writer, "read_template RunsAndBall");
//			
//			populateLastxovers(print_writer, match, Integer.valueOf(valueToProcess.split(",")[0]) ,category);
//			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[1]);
//			break;
		case "POPULATE_GRAPHICS_LASTXOVERS":
		   sendVizMSELastXOversPage(
		    		valueToProcess.split(",")[1],
		            match,
		            Integer.parseInt(valueToProcess.split(",")[0]),
		            ipAddress,
		            showname);

		    break;
			
//		case "POPULATE_GRAPHICS_COMPARISION":
//		    String template = valueToProcess.split(",")[1].equalsIgnoreCase("AR") 
//		        ? "read_template Comparison" 
//		        : "read_template Comparison-Drone";
//
//		    Inning inning2 = match.getMatch().getInning().get(1);
//		    boolean homeIsBatting = match.getSetup().getHomeTeamId() == inning2.getBattingTeamId();
//		    System.out.println( "category.toUpperCase()= "+ category.toUpperCase());
//		    DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " + category.toUpperCase());
//		    StringBuilder sb = new StringBuilder();
//		    sb.append(valueToProcess.split(",")[1].equalsIgnoreCase("AR") ? "read_template Comparison" : "read_template Comparison-Drone").append(return_key).append(line_feed);
//		    sb.append("tabfield:set_value_no_update 001-TEAMNAME ").append(homeIsBatting ? match.getSetup().getAwayTeam().getTeamBadge() : match.getSetup().getHomeTeam().getTeamBadge()).append(return_key).append(line_feed);
//		    sb.append("tabfield:set_value_no_update 002-TEAMNAME02 ").append(homeIsBatting ? match.getSetup().getHomeTeam().getTeamBadge() : match.getSetup().getAwayTeam().getTeamBadge()).append(return_key).append(line_feed);
//		    sb.append("tabfield:set_value_no_update 003-Header AFTER ").append(CricketFunctions.OverBalls(inning2.getTotalOvers(), inning2.getTotalBalls())).append(" OVERS").append(return_key).append(line_feed);
//		    sb.append("tabfield:set_value_no_update 101-RUNS01 ").append(IndexController.MatchStats.getInningCompare().getTotalRuns()).append("-").append(IndexController.MatchStats.getInningCompare().getTotalWickets()).append(return_key).append(line_feed);
//		    sb.append("tabfield:set_value_no_update 102-RUNS02 ").append(inning2.getTotalRuns()).append("-").append(inning2.getTotalWickets()).append(return_key).append(line_feed);
//		    sb.append("saveas ").append(valueToProcess.split(",")[0]).append(return_key).append(line_feed);
//
//		    print_writer.print(sb.toString());
//		    print_writer.flush();
//		    break;
//		case "POPULATE_GRAPHICS_TARGET":
//			if(valueToProcess.split(",")[1].equalsIgnoreCase("AR")){
//				DoadWriteToTrio(print_writer, "read_template Target");
//			}else {
//				DoadWriteToTrio(print_writer, "read_template Target_Drone");
//			}
//			
//			populateTarget(print_writer, match,category);
//			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[0]);
//			break;
		case "POPULATE_GRAPHICS_TARGET":
		    sendVizMSETargetPage(valueToProcess.split(",")[0], match, ipAddress, showname);
		    break;
//		case "POPULATE_GRAPHICS_PROJECTED":
//			if(valueToProcess.split(",")[1].equalsIgnoreCase("AR")){
//				DoadWriteToTrio(print_writer, "read_template Projected");
//			}else {
//				DoadWriteToTrio(print_writer, "read_template Projected_Score_Drone");
//			}
//			
//			populateProjected(print_writer, match,category);
//			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[0]);
//			break;
		case "POPULATE_GRAPHICS_PROJECTED":
		    sendVizMSEProjectedPage(valueToProcess.split(",")[0], match, ipAddress, showname);
		    break;
//		case "POPULATE-LASTXBALLS_VR":
//			if(valueToProcess.split(",")[1].equalsIgnoreCase("AR")){
//				DoadWriteToTrio(print_writer, "read_template RunsAndBall");
//			}else {
//				DoadWriteToTrio(print_writer, "read_template RunsAndBall");
//			}
//			
//			populateLASTXBALLS(print_writer, match,Integer.valueOf(valueToProcess.split(",")[0]));
//			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[2]);
//			break;	
		case "POPULATE-LASTXBALLS_VR":
		    sendVizMSELastXBallsPage(
		    		valueToProcess.split(",")[2],
		            match,
		            Integer.parseInt(valueToProcess.split(",")[0]),
		            ipAddress,
		            showname);

		    break;
//		case "POPULATE_GRAPHICS_TOSS":
//			DoadWriteToTrio(print_writer, "read_template TOSS");
//			populateToss(print_writer, match,category, Integer.parseInt(valueToProcess.split(",")[0]),valueToProcess.split(",")[1]);
//			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[2]);
//			break;
		case "POPULATE_GRAPHICS_TOSS":

		    sendVizMSETossPage(
		    		valueToProcess.split(",")[2],
		            match,
		            Integer.parseInt(valueToProcess.split(",")[0]),
		            valueToProcess.split(",")[1],
		            ipAddress,
		            showname);

		    break;
//		case "POPULATE_GRAPHICS_VCTORYY":
//			if(valueToProcess.split(",")[1].equalsIgnoreCase("AR")){
//				DoadWriteToTrio(print_writer, "read_template Victory");
//			}else {
//				DoadWriteToTrio(print_writer, "read_template Toss_Drone");
//			}
//			
//			populateVictory(print_writer, match,category);
//			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[0]);
//			break;
			
		case "POPULATE_GRAPHICS_VCTORYY":
		    sendVizMSEVictoryPage(valueToProcess.split(",")[0], match, ipAddress, showname);
		    break;
		case "POPULATE_GRAPHICS_MATCHID":
			if(valueToProcess.split(",")[1].equalsIgnoreCase("AR")){
				DoadWriteToTrio(print_writer, "read_template Ident");
			}else {
				DoadWriteToTrio(print_writer, "read_template MatchID_Drone");
			}
			populateMatchID(print_writer, match,cricketService,category);
			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[0]);
			break;
		case "POPULATE_GRAPHICS_UPLINE":
			if(valueToProcess.split(",")[1].equalsIgnoreCase("AR")){
				DoadWriteToTrio(print_writer, "read_template LineUp");
			}else {
				DoadWriteToTrio(print_writer, "read_template LineUp_Drone");
			}
			populateUPLINE(print_writer, match,cricketService);
			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[0]);
			break;
		case "POPULATE_GRAPHICS_CURRENTPARTERNERSHIP":
			if(valueToProcess.split(",")[1].equalsIgnoreCase("AR")){
				DoadWriteToTrio(print_writer, "read_template Partnership_ActionImage");
			}else {
				DoadWriteToTrio(print_writer, "read_template Partnership_ActionImage_Drone");
			}
			populatePARTNERSHIP(print_writer, match,cricketService);
			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[0]);
			break;
//		case "POPULATE_GRAPHICS_MOSTRUNS":
//			tournament_stats = new ArrayList<Tournament>();
//			tournament_stats = CricketFunctions.extractTournamentData("CURRENT_MATCH_DATA", false, headToHead.getH2hPlayer(), cricketService, 
//					match, pasttornament);
//			//System.out.println( "pastdata" +  pasttornament.size());
//			Collections.sort(tournament_stats,new CricketFunctions.BatsmenMostRunComparator());
//			if(valueToProcess.split(",")[1].equalsIgnoreCase("AR")){
//				DoadWriteToTrio(print_writer, "read_template Leaderboard");
//			}else {
//				DoadWriteToTrio(print_writer, "read_template Leaderboard");
//			}
//			populateMOSTRUNS(print_writer, match,cricketService,tournament_stats,valueToProcess.split(",")[2],category);
//			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[0]);
//			break;
			
		case "POPULATE_GRAPHICS_MOSTRUNS":

		    tournament_stats = CricketFunctions.extractTournamentData("CURRENT_MATCH_DATA",false,headToHead.getH2hPlayer(),cricketService,
		            match,pasttornament);

		    Collections.sort(tournament_stats,new CricketFunctions.BatsmenMostRunComparator()
		    );

		    sendVizMSEMostRunsPage( valueToProcess.split(",")[0],match,tournament_stats,cricketService,category,ipAddress,showname);

		    break;	
		case "POPULATE_GRAPHICS_MOSTWKTS":
			tournament_stats = new ArrayList<Tournament>();
			tournament_stats = CricketFunctions.extractTournamentData("CURRENT_MATCH_DATA", false, headToHead.getH2hPlayer(), cricketService, 
					match, pasttornament);
			Collections.sort(tournament_stats,new CricketFunctions.BowlerWicketsComparator());
			sendVizMSEMostWicketsPage(valueToProcess.split(",")[0],match,tournament_stats,cricketService,category,ipAddress,showname);
			break;	 
		case "POPULATE_GRAPHICS_TAPEBALL":
			List<BestStats> tapeball1 = new ArrayList<BestStats>();
			tapeball1 = CricketFunctions.extractTapeData("CURRENT_MATCH_DATA", cricketService, match, tapeball, headToHead.getH2hPlayer());
			Collections.sort(tapeball1,new CricketFunctions.TapeBowlerWicketsComparator());
			if(valueToProcess.split(",")[1].equalsIgnoreCase("AR")){
				DoadWriteToTrio(print_writer, "read_template LeaderBoard_ActionImage");
			}else {
				DoadWriteToTrio(print_writer, "read_template LeaderBoard_ActionImage_Drone");
			}
			populateTapeBallMostWickets(print_writer, match,cricketService,tapeball1,valueToProcess.split(",")[2]);
			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[0]);
			break;	
		
		case "POPULATE_GRAPHICS_MOSTNINE":
			tournament_stats = new ArrayList<Tournament>();
			tournament_stats = CricketFunctions.extractTournamentData("CURRENT_MATCH_DATA", false, headToHead.getH2hPlayer(), cricketService, 
					match, pasttornament);
			Collections.sort(tournament_stats,new CricketFunctions.BatsmanNinesComparator());
			if(valueToProcess.split(",")[1].equalsIgnoreCase("AR")){
				DoadWriteToTrio(print_writer, "read_template LeaderBoard_ActionImage");
			}else {
				DoadWriteToTrio(print_writer, "read_template LeaderBoard_ActionImage_Drone");
			}
			populateMOSTNINES(print_writer, match,cricketService,tournament_stats,valueToProcess.split(",")[2]);
			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[0]);
			break;	
		case "POPULATE_GRAPHICS_MOSTFOURS":
			tournament_stats = new ArrayList<Tournament>();
			tournament_stats = CricketFunctions.extractTournamentData("CURRENT_MATCH_DATA", false, headToHead.getH2hPlayer(), cricketService, 
					match, pasttornament);
			Collections.sort(tournament_stats,new CricketFunctions.BatsmanFoursComparator());
			if(valueToProcess.split(",")[1].equalsIgnoreCase("AR")){
				DoadWriteToTrio(print_writer, "read_template LeaderBoard_ActionImage");
			}else {
				DoadWriteToTrio(print_writer, "read_template LeaderBoard_ActionImage_Drone");
			}
			populateMOSTFOURS(print_writer, match,cricketService,tournament_stats,valueToProcess.split(",")[2]);
			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[0]);
			break;
		case "POPULATE_GRAPHICS_MOSTSIXES":
			tournament_stats = new ArrayList<Tournament>();
			tournament_stats = CricketFunctions.extractTournamentData("CURRENT_MATCH_DATA", false, headToHead.getH2hPlayer(), cricketService, 
					match, pasttornament);
			Collections.sort(tournament_stats,new CricketFunctions.BatsmanSixesComparator());
			if(valueToProcess.split(",")[1].equalsIgnoreCase("AR")){
				DoadWriteToTrio(print_writer, "read_template LeaderBoard_ActionImage");
			}else {
				DoadWriteToTrio(print_writer, "read_template LeaderBoard_ActionImage_Drone");
			}
			populateMOSTSIXES(print_writer, match,cricketService,tournament_stats,valueToProcess.split(",")[2]);
			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[0]);
			break;			
//		case "POPULATE_GRAPHICS_EQUATION":
//			if(valueToProcess.split(",")[1].equalsIgnoreCase("AR")){
//				DoadWriteToTrio(print_writer, "read_template RunsAndBall");
//			}else {
//				DoadWriteToTrio(print_writer, "read_template Equation_Drone");
//			}
//			
//			populateEquation(print_writer, match,IndexController.headToHead,category);
//			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[0]);
//			break;
			
		case "POPULATE_GRAPHICS_EQUATION":
		    sendVizMSEEquationPage(valueToProcess.split(",")[0], match,ipAddress,showname);
		    break;
		case "POPULATE_GRAPHICS_BOUNDARIES":
		    sendVizMSEBoundaryPage(valueToProcess.split(",")[0], match, Integer.valueOf(valueToProcess.split(",")[1]),ipAddress,showname);
		    break;
		case "POPULATE_GRAPHICS_COMPARISION":
		    sendVizMSEComparisonPage(valueToProcess.split(",")[0], match, ipAddress, showname);
		    break;
			
			 
		case "POPULATE_GRAPHICS_MVP":
			if(new File(mainCricketDirectory + CricketUtil.MVP).exists()) {
				mvp = (new ObjectMapper().readValue(new File(secondCricketDirectory + CricketUtil.MVP), mvp_leaderBoard.class));
			}
			
			if(valueToProcess.split(",")[1].equalsIgnoreCase("AR")){
				DoadWriteToTrio(print_writer, "read_template MVP_LEADERBOARD_New");
			}else {
				DoadWriteToTrio(print_writer, "read_template MVP_LEADERBOARD_New_Drone");
			}
			
			populateMVP(print_writer, match,cricketService);
			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[0]);
			break;
		case "POPULATE_GRAPHICS_MVP_LEADERBOARD":	
			if(new File(mainCricketDirectory + CricketUtil.MVP).exists()) {
				mvp = (new ObjectMapper().readValue(new File(secondCricketDirectory + CricketUtil.MVP), mvp_leaderBoard.class));
			}
			
			if(valueToProcess.split(",")[1].equalsIgnoreCase("AR")){
				DoadWriteToTrio(print_writer, "read_template LeaderBoard_ActionImage");
			}else {
				DoadWriteToTrio(print_writer, "read_template LeaderBoard_ActionImage_Drone");
			}
			
			populateMVPLEADERBOARD(print_writer, match,cricketService);
			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[0]);
			break;
//		case "POPULATE_GRAPHICS_REQUIREDRUNRATE":
//			if(valueToProcess.split(",")[1].equalsIgnoreCase("AR")){
//				DoadWriteToTrio(print_writer, "read_template RunsAndBall");
//			}else {
//				DoadWriteToTrio(print_writer, "read_template RunRates_Drone");
//			}
//			
//			populateRUNRATE(print_writer, match,cricketService,category);
//			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[0]);
//			break;
		case "POPULATE_GRAPHICS_REQUIREDRUNRATE":
		    sendVizMSERunRatePage(valueToProcess.split(",")[0], match, cricketService, ipAddress, showname);
		    break;	
			
		
//		case "POPULATE_GRAPHICS_BOUNDARIES":
//			DoadWriteToTrio(print_writer, "read_template RunsAndBall");
//			populateBoundaries(print_writer, match,cricketService,Integer.valueOf(valueToProcess.split(",")[1]),category);
//			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[0]);
//			break;
			
//		case "POPULATE_GRAPHICS_BOUNDARIES":
//		    String[] parts = valueToProcess.split(",");
//		    DoadWriteToTrio(print_writer, "read_template RunsAndBall");
//		    print_writer.flush();
//
//		    Thread.sleep(300); // give Trio time to finish loading template
//
//		    populateBoundaries(print_writer, match, cricketService, Integer.valueOf(parts[1]));
//		    DoadWriteToTrio(print_writer, "saveas " + parts[0]);
//		    break;
			
		case "POPULATE_DOUBLEMATCHID":	
			//System.out.println("Valuetoprocess " + valueToProcess);
			if(valueToProcess.split(",")[1].equalsIgnoreCase("AR")) {
				DoadWriteToTrio(print_writer, "read_template DoubleMatchID");
			}else {
				DoadWriteToTrio(print_writer, "read_template DoubleMatchID_Drone");
			}
			populateDoublematchid(print_writer, match,cricketService,valueToProcess.split(",")[0]);
			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[2]);
			break;	
				
		case "POPULATE_GRAPHICS_IN_AT":
			inning = match.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(valueToProcess.split(",")[3]))
			.findAny().orElse(null);
			player = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(valueToProcess.split(",")[0]), match);
			if(valueToProcess.split(",")[1].equalsIgnoreCase("AR")) {
				DoadWriteToTrio(print_writer, "read_template In_AT_02");
			}else {
				DoadWriteToTrio(print_writer, "read_template Profile_Drone");
			}
			
			populateInAT(print_writer, match,Integer.valueOf(valueToProcess.split(",")[3]),category);
			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[2]);
			break;
		case "POPULATE_GRAPHICS_BATPROFILE":
			 whichprofile =valueToProcess.split(",")[1].toUpperCase();
				inning = match.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(valueToProcess.split(",")[4]))
					.findAny().orElse(null);
				player = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(valueToProcess.split(",")[0]), match);
				
				String profileName = valueToProcess.split(",")[1].trim();
				List<StatsType> allTypes = cricketService.getAllStatsType();
				statsType = allTypes.stream()
				        .filter(stype ->
				            stype.getStats_short_name() != null &&
				            stype.getStats_short_name().equalsIgnoreCase(profileName))
				        .findAny()
				        .orElse(null);

				if(!valueToProcess.split(",")[1].equalsIgnoreCase("T20 MUMBAI")) {
					statsType = cricketService.getAllStatsType().stream().filter(stype -> stype.getStats_short_name().
							equalsIgnoreCase(valueToProcess.split(",")[1].trim())).findAny().orElse(null);
					stat = cricketService.getAllStats().stream().filter(st -> st.getPlayer_id() == player.getPlayerId() && statsType.getStats_id() == st.getStats_type_id()).findAny().orElse(null);
					stat.setStats_type(statsType);
				}
			
				switch (valueToProcess.split(",")[1].toUpperCase()) {
				case "T20 MUMBAI":
					statsType = cricketService.getAllStatsType().stream().filter(st -> st.getStats_short_name().equalsIgnoreCase("T20 MUMBAI")).findAny().orElse(null);
					stat = cricketService.getAllStats().stream().filter(st -> st.getPlayer_id() == player.getPlayerId() && statsType.getStats_id() == st.getStats_type_id()).findAny().orElse(null);
					
					statsType = cricketService.getAllStatsType().stream().filter(st -> st.getStats_short_name().equalsIgnoreCase("DT20")).findAny().orElse(null);
					stat.setStats_type(statsType);
					
					stat = CricketFunctions.updateTournamentWithH2h(stat, headToHead.getH2hPlayer(), match, CricketUtil.FULL);
					stat = CricketFunctions.updateStatisticsWithMatchData(stat, match, CricketUtil.FULL);
					break;
				}
//				team = Teams.stream().filter(tm -> tm.getTeamId() == player.getTeamId()).findAny().orElse(null);
//				if(team == null) {
//					return "populatePlayerProfile: Team id [" + player.getTeamId() + "] from database is returning NULL";
//				}
			if(valueToProcess.split(",")[2].equalsIgnoreCase("AR")) {
				DoadWriteToTrio(print_writer, "read_template In_AT_Profile");
			}else {
				DoadWriteToTrio(print_writer, "read_template Profile_Drone");
			}
			
			populateProfile(print_writer, match,Integer.valueOf(valueToProcess.split(",")[4]),category);
			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[3]);
			break;
		case "POPULATE_GRAPHICS_AUCTIONBATPROFILE":
			debut = "NO";
			for(Player ply: cricketService.getAllPlayer()) {
				if(ply.getPlayerId() == Integer.valueOf(valueToProcess.split(",")[0])) {
					if(ply.getDebut().equalsIgnoreCase("YES")) {
						debut = "YES";
						player = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(valueToProcess.split(",")[0]), match);
						if(match.getSetup().getHomeTeamId() == player.getTeamId()) {
							team = match.getSetup().getHomeTeam();
						} else if(match.getSetup().getAwayTeamId() == player.getTeamId()) {
							team = match.getSetup().getAwayTeam();
						}
					}else {
						player = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(valueToProcess.split(",")[0]), match);
						if(valueToProcess.split(",")[1].equalsIgnoreCase("ISPL_CAREER")) {
							profile = "ISPL CAREER";
						}else if(valueToProcess.split(",")[1].equalsIgnoreCase("ISPL S1")){
							profile = "ISPL SEASON 1";
						}else if(valueToProcess.split(",")[1].equalsIgnoreCase("ISPL S2")){
							profile = "ISPL SEASON 2";
						}else if(valueToProcess.split(",")[1].equalsIgnoreCase("THIS SERIES")){
							profile = "ISPL SEASON 3";
							PastDataToCrr = CricketFunctions.extractTournamentData("CURRENT_MATCH_DATA", false, headToHead.getH2hPlayer(), 
									cricketService, match, pasttornament);
							
							top_Batsman = new ArrayList<BestStats>();
							topbowler = new ArrayList<BestStats>();
							for(Tournament tour: PastDataToCrr) {
								for(BestStats bs:tour.getBatsman_best_Stats()) {
									top_Batsman.add(bs);
								}
								for(BestStats bfig: tour.getBowler_best_Stats()) {
									topbowler.add(bfig);
								}
							}
							//System.out.println(PastDataToCrr.size());
							Collections.sort(top_Batsman,new CricketFunctions.PlayerBestStatsComparator());
							Collections.sort(topbowler,new CricketFunctions.PlayerBestStatsComparator());
							
							tournament = PastDataToCrr.stream().filter(tour->tour.getPlayerId() == player.getPlayerId()).findAny().orElse(null);
						}
						
						if(match.getSetup().getHomeTeamId() == player.getTeamId()) {
							team = match.getSetup().getHomeTeam();
						} else if(match.getSetup().getAwayTeamId() == player.getTeamId()) {
							team = match.getSetup().getAwayTeam();
						}
						if(valueToProcess.split(",")[1].equalsIgnoreCase("ISPL S1") || valueToProcess.split(",")[1].equalsIgnoreCase("ISPL S2")) {
							statsType = cricketService.getAllStatsType().stream().filter(stype -> stype.getStats_short_name().equalsIgnoreCase(valueToProcess.split(",")[1])).findAny().orElse(null);
							if(statsType == null) {
								return "PopulateL3rdPlayerProfile: Stats Type not found for profile [" + valueToProcess.split(",")[1] + "]";
							}
							stat = cricketService.getAllStats().stream().filter(st -> st.getPlayer_id() == player.getPlayerId() && statsType.getStats_id() == st.getStats_type_id()).findAny().orElse(null);
							if(stat == null) {
								return "populatePlayerProfile: No stats found for player id [" + player.getFull_name() + "] from database is returning NULL";
							}
							stat.setStats_type(statsType);
						}
						if(valueToProcess.split(",")[1].equalsIgnoreCase("ISPL_CAREER")) {
							statsType = new StatsType();
							stat = new Statistics();
							
							Statistics statS1 = CricketFunctions.getStatsByType(Integer.valueOf(valueToProcess.split(",")[0]), "ISPL S1", cricketService.getAllStatsType(), cricketService.getAllStats());
						    Statistics statS2 = CricketFunctions.getStatsByType(Integer.valueOf(valueToProcess.split(",")[0]), "ISPL S2", cricketService.getAllStatsType(), cricketService.getAllStats());
						    
						    if (statS1 == null && statS2 == null) {
						        return "InfoBarPlayerProfile: Stats not found for Player Id [" + valueToProcess.split(",")[0] + "]";
						    }
						    stat = statS1 != null ? statS1 : statS2;
						    stat = CricketFunctions.mergeIsplCareerStats(stat, statS2);
							
							statsType = cricketService.getAllStatsType().stream().filter(st -> st.getStats_short_name().equalsIgnoreCase("D10")).findAny().orElse(null);
							stat.setStats_type(statsType);
							
							//System.out.println(stat.getMatches());
							stat = CricketFunctions.updateTournamentWithH2h(stat, headToHead.getH2hPlayer(), match, CricketUtil.FULL);
							stat = CricketFunctions.updateStatisticsWithMatchData(stat, match, CricketUtil.FULL);
						}
						
						if(valueToProcess.split(",")[1].equalsIgnoreCase("THIS SERIES")) {
							//System.out.println("CP");
							for(int i=0;i<=top_Batsman.size()-1;i++) {
								if(top_Batsman.get(i).getPlayerId() == player.getPlayerId()) {
									if(top_Batsman.get(i).getBestEquation() % 2 == 0) {
										if(top_Batsman.get(i).getBestEquation() / 2  == 0) {
											best = "-";
										}else {
											best = String.valueOf(top_Batsman.get(i).getBestEquation() /2);
										}
									}else {
										best = (top_Batsman.get(i).getBestEquation()-1) / 2 + "*";
									}
									break;
								}else {
									best = "-";
								}
							}
						}else {
							if(stat.getRuns_conceded() == 0 || stat.getWickets() == 0) {
								Data = "-";
							}else {
								average = stat.getRuns_conceded()/stat.getWickets();
								DecimalFormat df_bo = new DecimalFormat("0.00");
								Data = df_bo.format(average);
							}
							if(stat.getFifties() == 0) {
								fifty = "-";
							}else {
								fifty = String.valueOf(stat.getFifties());
							}
							
							if(stat.getThirties() == 0) {
								thirty = "-";
							}else {
								thirty = String.valueOf(stat.getThirties());
							}
							
							if(stat.getHundreds() == 0) {
								hundred = "-";
							}else {
								hundred = String.valueOf(stat.getHundreds());
							}
							
							if(fifty.equalsIgnoreCase("-") && !hundred.equalsIgnoreCase("-")) {
								fifty = "0";
							}
							
							if(hundred.equalsIgnoreCase("-") && !fifty.equalsIgnoreCase("-")) {
								hundred = "0";
							}
							
							if(CricketFunctions.generateStrikeRate(stat.getRuns(), stat.getBalls_faced(), 1).trim().isEmpty()) {
								strikeRate = "-";
							}else {
								strikeRate = String.valueOf(CricketFunctions.generateStrikeRate(stat.getRuns(), stat.getBalls_faced(), 0));
							}
							
							if(stat.getRuns() == 0) {
								runs = "-";
							}else {
								runs = String.format("%,d\n", stat.getRuns());
							}
							
							if(CricketFunctions.getAverage(stat.getInnings(), stat.getNot_out(), stat.getRuns(), 2, "-").equalsIgnoreCase("0.00")) {
								batAverage = "-";
							}else {
								batAverage = CricketFunctions.getAverage(stat.getInnings(), stat.getNot_out(), stat.getRuns(), 2, "-");
							}
							
							if(player.getSurname() == null) {
								surName = "";
							}else {
								surName = player.getSurname();
							}
							
							if(stat.getBest_score().equalsIgnoreCase("0")) {
								best = "-";
							}else {
								best = stat.getBest_score();
							}
						}
					}
						}
					}
			
			if(valueToProcess.split(",")[2].equalsIgnoreCase("AR")) {
				DoadWriteToTrio(print_writer, "read_template Profile_ActionImage");
			}else {
				DoadWriteToTrio(print_writer, "read_template Profile_ActionImageDrone");
			}
			
			populateProfileAuction(print_writer, match,Integer.valueOf(valueToProcess.split(",")[4]));
			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[3]);
			break;
			
//		case "POPULATE_GRAPHICS_OPENERBATPROFILE":
//			//System.out.println("whattoprocess" + valueToProcess);
//			debut1 ="NO";
//			debut2 ="NO";
//			for(Player ply: cricketService.getAllPlayer()) {
//				if(ply.getPlayerId() == Integer.valueOf(valueToProcess.split(",")[0])) {
//					if(ply.getDebut().equalsIgnoreCase("YES")) {
//						debut1 = "YES";
//						
//					}else {
//						player = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(valueToProcess.split(",")[0]), match);
//						player2 = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(valueToProcess.split(",")[1]), match);
//						if(valueToProcess.split(",")[2].equalsIgnoreCase("ISPL_CAREER")) {
//							profile = "ISPL CAREER";
//						}else if(valueToProcess.split(",")[2].equalsIgnoreCase("ISPL S1")){
//							profile = "ISPL SEASON 1";
//						}else if(valueToProcess.split(",")[2].equalsIgnoreCase("ISPL S2")){
//							profile = "ISPL SEASON 2";
//						}else if(valueToProcess.split(",")[2].equalsIgnoreCase("THIS SERIES")){
//							profile = "ISPL SEASON 3";
//							PastDataToCrr = CricketFunctions.extractTournamentData("CURRENT_MATCH_DATA", false,  headToHead.getH2hPlayer(), cricketService, match, pasttornament);
//							
//							top_Batsman = new ArrayList<BestStats>();
//							for(Tournament tour: PastDataToCrr) {
//								for(BestStats bs:tour.getBatsman_best_Stats()) {
//									top_Batsman.add(bs);
//								}
//							}
//							
//							//System.out.println(PastDataToCrr.size());
//							Collections.sort(top_Batsman,new CricketFunctions.PlayerBestStatsComparator());
//							
//							tournament = PastDataToCrr.stream().filter(tour->tour.getPlayerId() == player.getPlayerId()).findAny().orElse(null);
//							tournament2 = PastDataToCrr.stream().filter(tour->tour.getPlayerId() == player2.getPlayerId()).findAny().orElse(null);
//						}
//						
//						if(match.getSetup().getHomeTeamId() == player.getTeamId()) {
//							team = match.getSetup().getHomeTeam();
//						} else if(match.getSetup().getAwayTeamId() == player.getTeamId()) {
//							team = match.getSetup().getAwayTeam();
//						}
//						if(valueToProcess.split(",")[2].equalsIgnoreCase("ISPL S1") || valueToProcess.split(",")[2].equalsIgnoreCase("ISPL S2")) {
//							statsType = cricketService.getAllStatsType().stream().filter(stype -> stype.getStats_short_name().equalsIgnoreCase(valueToProcess.split(",")[2])).findAny().orElse(null);
//							statsType2 = cricketService.getAllStatsType().stream().filter(stype -> stype.getStats_short_name().equalsIgnoreCase(valueToProcess.split(",")[2])).findAny().orElse(null);
//							if(statsType == null) {
//								return "PopulateL3rdPlayerProfile: Stats Type not found for profile [" + valueToProcess.split(",")[2] + "]";
//							}
//							stat = cricketService.getAllStats().stream().filter(st -> st.getPlayer_id() == player.getPlayerId() && statsType.getStats_id() == st.getStats_type_id()).findAny().orElse(null);
//							if(stat == null) {
//								return "populatePlayerProfile: No stats found for player id [" + player.getFull_name() + "] from database is returning NULL";
//							}
//							stat.setStats_type(statsType);
//							
//							//for player2
//							if(statsType2 == null) {
//								return "PopulateL3rdPlayerProfile: Stats Type not found for profile [" + valueToProcess.split(",")[2] + "]";
//							}
//							stat2 = cricketService.getAllStats().stream().filter(st -> st.getPlayer_id() == player2.getPlayerId() && statsType2.getStats_id() == st.getStats_type_id()).findAny().orElse(null);
//							if(stat2 == null) {
//								return "populatePlayerProfile: No stats found for player id [" + player2.getFull_name() + "] from database is returning NULL";
//							}
//							stat2.setStats_type(statsType2);
//						}
//						if(valueToProcess.split(",")[2].equalsIgnoreCase("ISPL_CAREER")) {
//							statsType = new StatsType();
//							stat = new Statistics();
//							
//							Statistics statS1 = CricketFunctions.getStatsByType(Integer.valueOf(valueToProcess.split(",")[0]), "ISPL S1", cricketService.getAllStatsType(), cricketService.getAllStats());
//						    Statistics statS2 = CricketFunctions.getStatsByType(Integer.valueOf(valueToProcess.split(",")[0]), "ISPL S2", cricketService.getAllStatsType(), cricketService.getAllStats());
//						    
//						    if (statS1 == null && statS2 == null) {
//						        return "InfoBarPlayerProfile: Stats not found for Player Id [" + valueToProcess.split(",")[0] + "]";
//						    }
//						    stat = statS1 != null ? statS1 : statS2;
//						    stat = CricketFunctions.mergeIsplCareerStats(stat, statS2);
//							
//							statsType = cricketService.getAllStatsType().stream().filter(st -> st.getStats_short_name().equalsIgnoreCase("D10")).findAny().orElse(null);
//							stat.setStats_type(statsType);
//							
//							//System.out.println(stat.getMatches());
//							stat = CricketFunctions.updateTournamentWithH2h(stat, headToHead.getH2hPlayer(), match, CricketUtil.FULL);
//							stat = CricketFunctions.updateStatisticsWithMatchData(stat, match, CricketUtil.FULL);
//							
//							//2
//							statsType2 = new StatsType();
//							stat2 = new Statistics();
//							
//							Statistics statSE1 = CricketFunctions.getStatsByType(Integer.valueOf(valueToProcess.split(",")[1]), "ISPL S1", cricketService.getAllStatsType(), cricketService.getAllStats());
//						    Statistics statSE2 = CricketFunctions.getStatsByType(Integer.valueOf(valueToProcess.split(",")[1]), "ISPL S2", cricketService.getAllStatsType(), cricketService.getAllStats());
//						    
//						    if (statSE1 == null && statSE2 == null) {
//						        return "InfoBarPlayerProfile: Stats not found for Player Id [" + valueToProcess.split(",")[1] + "]";
//						    }
//						    stat2 = statSE1 != null ? statSE1 : statSE2;
//						    stat2 = CricketFunctions.mergeIsplCareerStats(stat2, statSE2);
//							
//							statsType2 = cricketService.getAllStatsType().stream().filter(st -> st.getStats_short_name().equalsIgnoreCase("D10")).findAny().orElse(null);
//							stat2.setStats_type(statsType2);
//							
//							//System.out.println(stat2.getMatches());
//							stat2 = CricketFunctions.updateTournamentWithH2h(stat2, headToHead.getH2hPlayer(), match, CricketUtil.FULL);
//							stat2 = CricketFunctions.updateStatisticsWithMatchData(stat2, match, CricketUtil.FULL);
//						}
//						
//						if(valueToProcess.split(",")[2].equalsIgnoreCase("THIS SERIES")) {
//							for(int i=0;i<=top_Batsman.size()-1;i++) {
//								if(top_Batsman.get(i).getPlayerId() == player.getPlayerId()) {
//									if(top_Batsman.get(i).getBestEquation() % 2 == 0) {
//										if(top_Batsman.get(i).getBestEquation() / 2  == 0) {
//											best = "-";
//										}else {
//											best = String.valueOf(top_Batsman.get(i).getBestEquation() /2);
//										}
//									}else {
//										best = (top_Batsman.get(i).getBestEquation()-1) / 2 + "*";
//									}
//									break;
//								}else {
//									best = "-";
//								}
//								
//								//2
//								if(top_Batsman.get(i).getPlayerId() == player2.getPlayerId()) {
//									if(top_Batsman.get(i).getBestEquation() % 2 == 0) {
//										if(top_Batsman.get(i).getBestEquation() / 2  == 0) {
//											best2 = "-";
//										}else {
//											best2 = String.valueOf(top_Batsman.get(i).getBestEquation() /2);
//										}
//									}else {
//										best2 = (top_Batsman.get(i).getBestEquation()-1) / 2 + "*";
//									}
//									break;
//								}else {
//									best2 = "-";
//								}
//							}
//						}else {
//							if(stat.getRuns_conceded() == 0 || stat.getWickets() == 0) {
//								Data = "-";
//							}else {
//								average = stat.getRuns_conceded()/stat.getWickets();
//								DecimalFormat df_bo = new DecimalFormat("0.00");
//								Data = df_bo.format(average);
//							}
//							if(stat.getFifties() == 0) {
//								fifty = "-";
//							}else {
//								fifty = String.valueOf(stat.getFifties());
//							}
//							
//							if(stat.getThirties() == 0) {
//								thirty = "-";
//							}else {
//								thirty = String.valueOf(stat.getThirties());
//							}
//							
//							if(stat.getHundreds() == 0) {
//								hundred = "-";
//							}else {
//								hundred = String.valueOf(stat.getHundreds());
//							}
//							
//							if(fifty.equalsIgnoreCase("-") && !hundred.equalsIgnoreCase("-")) {
//								fifty = "0";
//							}
//							
//							if(hundred.equalsIgnoreCase("-") && !fifty.equalsIgnoreCase("-")) {
//								hundred = "0";
//							}
//							
//							if(CricketFunctions.generateStrikeRate(stat.getRuns(), stat.getBalls_faced(), 1).trim().isEmpty()) {
//								strikeRate = "-";
//							}else {
//								strikeRate = String.valueOf(CricketFunctions.generateStrikeRate(stat.getRuns(), stat.getBalls_faced(), 0));
//							}
//							
//							if(stat.getRuns() == 0) {
//								runs = "-";
//							}else {
//								runs = String.format("%,d\n", stat.getRuns());
//							}
//							
//							if(CricketFunctions.getAverage(stat.getInnings(), stat.getNot_out(), stat.getRuns(), 2, "-").equalsIgnoreCase("0.00")) {
//								batAverage = "-";
//							}else {
//								batAverage = CricketFunctions.getAverage(stat.getInnings(), stat.getNot_out(), stat.getRuns(), 2, "-");
//							}
//							
//							if(player.getSurname() == null) {
//								surName = "";
//							}else {
//								surName = player.getSurname();
//							}
//							
//							if(stat.getBest_score().equalsIgnoreCase("0")) {
//								best = "-";
//							}else {
//								best = stat.getBest_score();
//							}
//							
//							//2
//							
//							if(stat2.getRuns_conceded() == 0 || stat2.getWickets() == 0) {
//								Data2 = "-";
//							}else {
//								average2 = stat2.getRuns_conceded()/stat2.getWickets();
//								DecimalFormat df_bo = new DecimalFormat("0.00");
//								Data2 = df_bo.format(average2);
//							}
//							
//							
//							if(CricketFunctions.generateStrikeRate(stat2.getRuns(), stat2.getBalls_faced(), 1).trim().isEmpty()) {
//								strikeRate2 = "-";
//							}else {
//								strikeRate2 = String.valueOf(CricketFunctions.generateStrikeRate(stat2.getRuns(), stat2.getBalls_faced(), 0));
//							}
//							
//							if(stat2.getRuns() == 0) {
//								runs2 = "-";
//							}else {
//								runs2 = String.format("%,d\n", stat2.getRuns());
//							}
//							
//							if(stat2.getBest_score().equalsIgnoreCase("0")) {
//								best2 = "-";
//							}else {
//								best2 = stat2.getBest_score();
//							}
//
//						}
//					}
//					
//				}
//				if(ply.getPlayerId() == Integer.valueOf(valueToProcess.split(",")[1])) {
//					if(ply.getDebut().equalsIgnoreCase("YES")) {
//						debut2 = "YES";
//					}else {
//						player = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(valueToProcess.split(",")[0]), match);
//						player2 = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(valueToProcess.split(",")[1]), match);
//						if(valueToProcess.split(",")[2].equalsIgnoreCase("ISPL_CAREER")) {
//							profile = "ISPL CAREER";
//						}else if(valueToProcess.split(",")[2].equalsIgnoreCase("ISPL S1")){
//							profile = "ISPL SEASON 1";
//						}else if(valueToProcess.split(",")[2].equalsIgnoreCase("ISPL S2")){
//							profile = "ISPL SEASON 2";
//						}else if(valueToProcess.split(",")[2].equalsIgnoreCase("THIS SERIES")){
//							profile = "ISPL SEASON 3";
//							PastDataToCrr = CricketFunctions.extractTournamentData("CURRENT_MATCH_DATA", false,  headToHead.getH2hPlayer(), cricketService, match, pasttornament);
//							
//							top_Batsman = new ArrayList<BestStats>();
//							for(Tournament tour: PastDataToCrr) {
//								for(BestStats bs:tour.getBatsman_best_Stats()) {
//									top_Batsman.add(bs);
//								}
//							}
//							
//							//System.out.println(PastDataToCrr.size());
//							Collections.sort(top_Batsman,new CricketFunctions.PlayerBestStatsComparator());
//							
//							tournament = PastDataToCrr.stream().filter(tour->tour.getPlayerId() == player.getPlayerId()).findAny().orElse(null);
//							tournament2 = PastDataToCrr.stream().filter(tour->tour.getPlayerId() == player2.getPlayerId()).findAny().orElse(null);
//						}
//						
//						if(match.getSetup().getHomeTeamId() == player.getTeamId()) {
//							team = match.getSetup().getHomeTeam();
//						} else if(match.getSetup().getAwayTeamId() == player.getTeamId()) {
//							team = match.getSetup().getAwayTeam();
//						}
//						if(valueToProcess.split(",")[2].equalsIgnoreCase("ISPL S1") || valueToProcess.split(",")[2].equalsIgnoreCase("ISPL S2")) {
//							statsType = cricketService.getAllStatsType().stream().filter(stype -> stype.getStats_short_name().equalsIgnoreCase(valueToProcess.split(",")[2])).findAny().orElse(null);
//							statsType2 = cricketService.getAllStatsType().stream().filter(stype -> stype.getStats_short_name().equalsIgnoreCase(valueToProcess.split(",")[2])).findAny().orElse(null);
//							if(statsType == null) {
//								return "PopulateL3rdPlayerProfile: Stats Type not found for profile [" + valueToProcess.split(",")[2] + "]";
//							}
//							stat = cricketService.getAllStats().stream().filter(st -> st.getPlayer_id() == player.getPlayerId() && statsType.getStats_id() == st.getStats_type_id()).findAny().orElse(null);
//							if(stat == null) {
//								return "populatePlayerProfile: No stats found for player id [" + player.getFull_name() + "] from database is returning NULL";
//							}
//							stat.setStats_type(statsType);
//							
//							//for player2
//							if(statsType2 == null) {
//								return "PopulateL3rdPlayerProfile: Stats Type not found for profile [" + valueToProcess.split(",")[2] + "]";
//							}
//							stat2 = cricketService.getAllStats().stream().filter(st -> st.getPlayer_id() == player2.getPlayerId() && statsType2.getStats_id() == st.getStats_type_id()).findAny().orElse(null);
//							if(stat2 == null) {
//								return "populatePlayerProfile: No stats found for player id [" + player2.getFull_name() + "] from database is returning NULL";
//							}
//							stat2.setStats_type(statsType2);
//						}
//						if(valueToProcess.split(",")[2].equalsIgnoreCase("ISPL_CAREER")) {
//							statsType = new StatsType();
//							stat = new Statistics();
//							
//							Statistics statS1 = CricketFunctions.getStatsByType(Integer.valueOf(valueToProcess.split(",")[0]), "ISPL S1", cricketService.getAllStatsType(), cricketService.getAllStats());
//						    Statistics statS2 = CricketFunctions.getStatsByType(Integer.valueOf(valueToProcess.split(",")[0]), "ISPL S2", cricketService.getAllStatsType(), cricketService.getAllStats());
//						    
//						    if (statS1 == null && statS2 == null) {
//						        return "InfoBarPlayerProfile: Stats not found for Player Id [" + valueToProcess.split(",")[0] + "]";
//						    }
//						    stat = statS1 != null ? statS1 : statS2;
//						    stat = CricketFunctions.mergeIsplCareerStats(stat, statS2);
//							
//							statsType = cricketService.getAllStatsType().stream().filter(st -> st.getStats_short_name().equalsIgnoreCase("D10")).findAny().orElse(null);
//							stat.setStats_type(statsType);
//							
//							//System.out.println(stat.getMatches());
//							stat = CricketFunctions.updateTournamentWithH2h(stat, headToHead.getH2hPlayer(), match, CricketUtil.FULL);
//							stat = CricketFunctions.updateStatisticsWithMatchData(stat, match, CricketUtil.FULL);
//							
//							//2
//							statsType2 = new StatsType();
//							stat2 = new Statistics();
//							
//							Statistics statSE1 = CricketFunctions.getStatsByType(Integer.valueOf(valueToProcess.split(",")[1]), "ISPL S1", cricketService.getAllStatsType(), cricketService.getAllStats());
//						    Statistics statSE2 = CricketFunctions.getStatsByType(Integer.valueOf(valueToProcess.split(",")[1]), "ISPL S2", cricketService.getAllStatsType(), cricketService.getAllStats());
//						    
//						    if (statSE1 == null && statSE2 == null) {
//						        return "InfoBarPlayerProfile: Stats not found for Player Id [" + valueToProcess.split(",")[1] + "]";
//						    }
//						    stat2 = statSE1 != null ? statSE1 : statSE2;
//						    stat2 = CricketFunctions.mergeIsplCareerStats(stat2, statSE2);
//							
//							statsType2 = cricketService.getAllStatsType().stream().filter(st -> st.getStats_short_name().equalsIgnoreCase("D10")).findAny().orElse(null);
//							stat2.setStats_type(statsType2);
//							
//							//System.out.println(stat2.getMatches());
//							stat2 = CricketFunctions.updateTournamentWithH2h(stat2, headToHead.getH2hPlayer(), match, CricketUtil.FULL);
//							stat2 = CricketFunctions.updateStatisticsWithMatchData(stat2, match, CricketUtil.FULL);
//						}
//						
//						if(valueToProcess.split(",")[2].equalsIgnoreCase("THIS SERIES")) {
//							for(int i=0;i<=top_Batsman.size()-1;i++) {
//								if(top_Batsman.get(i).getPlayerId() == player.getPlayerId()) {
//									if(top_Batsman.get(i).getBestEquation() % 2 == 0) {
//										if(top_Batsman.get(i).getBestEquation() / 2  == 0) {
//											best = "-";
//										}else {
//											best = String.valueOf(top_Batsman.get(i).getBestEquation() /2);
//										}
//									}else {
//										best = (top_Batsman.get(i).getBestEquation()-1) / 2 + "*";
//									}
//									break;
//								}else {
//									best = "-";
//								}
//								
//								//2
//								if(top_Batsman.get(i).getPlayerId() == player2.getPlayerId()) {
//									if(top_Batsman.get(i).getBestEquation() % 2 == 0) {
//										if(top_Batsman.get(i).getBestEquation() / 2  == 0) {
//											best2 = "-";
//										}else {
//											best2 = String.valueOf(top_Batsman.get(i).getBestEquation() /2);
//										}
//									}else {
//										best2 = (top_Batsman.get(i).getBestEquation()-1) / 2 + "*";
//									}
//									break;
//								}else {
//									best2 = "-";
//								}
//							}
//						}else {
//							if(stat.getRuns_conceded() == 0 || stat.getWickets() == 0) {
//								Data = "-";
//							}else {
//								average = stat.getRuns_conceded()/stat.getWickets();
//								DecimalFormat df_bo = new DecimalFormat("0.00");
//								Data = df_bo.format(average);
//							}
//							if(stat.getFifties() == 0) {
//								fifty = "-";
//							}else {
//								fifty = String.valueOf(stat.getFifties());
//							}
//							
//							if(stat.getThirties() == 0) {
//								thirty = "-";
//							}else {
//								thirty = String.valueOf(stat.getThirties());
//							}
//							
//							if(stat.getHundreds() == 0) {
//								hundred = "-";
//							}else {
//								hundred = String.valueOf(stat.getHundreds());
//							}
//							
//							if(fifty.equalsIgnoreCase("-") && !hundred.equalsIgnoreCase("-")) {
//								fifty = "0";
//							}
//							
//							if(hundred.equalsIgnoreCase("-") && !fifty.equalsIgnoreCase("-")) {
//								hundred = "0";
//							}
//							
//							if(CricketFunctions.generateStrikeRate(stat.getRuns(), stat.getBalls_faced(), 1).trim().isEmpty()) {
//								strikeRate = "-";
//							}else {
//								strikeRate = String.valueOf(CricketFunctions.generateStrikeRate(stat.getRuns(), stat.getBalls_faced(), 0));
//							}
//							
//							if(stat.getRuns() == 0) {
//								runs = "-";
//							}else {
//								runs = String.format("%,d\n", stat.getRuns());
//							}
//							
//							if(CricketFunctions.getAverage(stat.getInnings(), stat.getNot_out(), stat.getRuns(), 2, "-").equalsIgnoreCase("0.00")) {
//								batAverage = "-";
//							}else {
//								batAverage = CricketFunctions.getAverage(stat.getInnings(), stat.getNot_out(), stat.getRuns(), 2, "-");
//							}
//							
//							if(player.getSurname() == null) {
//								surName = "";
//							}else {
//								surName = player.getSurname();
//							}
//							
//							if(stat.getBest_score().equalsIgnoreCase("0")) {
//								best = "-";
//							}else {
//								best = stat.getBest_score();
//							}
//							
//							//2
//							if(stat2.getRuns_conceded() == 0 || stat2.getWickets() == 0) {
//								Data2 = "-";
//							}else {
//								average2 = stat2.getRuns_conceded()/stat2.getWickets();
//								DecimalFormat df_bo = new DecimalFormat("0.00");
//								Data2 = df_bo.format(average2);
//							}
//							
//							
//							if(CricketFunctions.generateStrikeRate(stat2.getRuns(), stat2.getBalls_faced(), 1).trim().isEmpty()) {
//								strikeRate2 = "-";
//							}else {
//								strikeRate2 = String.valueOf(CricketFunctions.generateStrikeRate(stat2.getRuns(), stat2.getBalls_faced(), 0));
//							}
//							
//							if(stat2.getRuns() == 0) {
//								runs2 = "-";
//							}else {
//								runs2 = String.format("%,d\n", stat2.getRuns());
//							}
//							
//							if(stat2.getBest_score().equalsIgnoreCase("0")) {
//								best2 = "-";
//							}else {
//								best2 = stat2.getBest_score();
//							}
//
//						}
//					}
//				}
//			}
//			if(valueToProcess.split(",")[3].equalsIgnoreCase("AR")) {
//				DoadWriteToTrio(print_writer, "read_template OpenerProfile");
//			}else {
//				DoadWriteToTrio(print_writer, "read_template OpenerProfile_Drone");
//			}
//			
//			populateOpenerProfile(print_writer, match,Integer.valueOf(valueToProcess.split(",")[5]));
//			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[4]);
//			break;
			
		case "POPULATE_GRAPHICS_OPENERBATPROFILE":
		    player = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(valueToProcess.split(",")[0]), match);
		    player2 = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(valueToProcess.split(",")[1]), match);

		    if(match.getSetup().getHomeTeamId() == player.getTeamId()) {
		        team = match.getSetup().getHomeTeam();
		    } else if(match.getSetup().getAwayTeamId() == player.getTeamId()) {
		        team = match.getSetup().getAwayTeam();
		    }

		    if(player.getSurname() == null) {
		        surName = "";
		    } else {
		        surName = player.getSurname();
		    }

		    if(valueToProcess.split(",")[2].equalsIgnoreCase("AR")) {
		        DoadWriteToTrio(print_writer, "read_template Opener");
		    } else {
		        DoadWriteToTrio(print_writer, "read_template OpenerProfile_Drone");
		    }

		    populateOpenerProfile(print_writer, match, Integer.valueOf(valueToProcess.split(",")[4]),category);
		    DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[3]);
		    break;	
		case "POPULATE_GRAPHICS_DOUBLEBATPROFILE":
			//System.out.println("Valuetoprocess" + valueToProcess);
			//System.out.println("whattoprocess" + valueToProcess);
			player = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(valueToProcess.split(",")[0]), match);
			player2 = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(valueToProcess.split(",")[1]), match);
			if(valueToProcess.split(",")[2].equalsIgnoreCase("ISPL_CAREER")) {
				profile = "ISPL CAREER";
			}else if(valueToProcess.split(",")[2].equalsIgnoreCase("ISPL S1")){
				profile = "ISPL SEASON 1";
			}else if(valueToProcess.split(",")[2].equalsIgnoreCase("ISPL S2")){
				profile = "ISPL SEASON 2";
			}else if(valueToProcess.split(",")[2].equalsIgnoreCase("THIS SERIES")){
				profile = "ISPL SEASON 3";
				PastDataToCrr = CricketFunctions.extractTournamentData("CURRENT_MATCH_DATA", false,  headToHead.getH2hPlayer(), cricketService, match, pasttornament);
				
				top_Batsman = new ArrayList<BestStats>();
				for(Tournament tour: PastDataToCrr) {
					for(BestStats bs:tour.getBatsman_best_Stats()) {
						top_Batsman.add(bs);
					}
				}
				
				//System.out.println(PastDataToCrr.size());
				Collections.sort(top_Batsman,new CricketFunctions.PlayerBestStatsComparator());
				
				tournament = PastDataToCrr.stream().filter(tour->tour.getPlayerId() == player.getPlayerId()).findAny().orElse(null);
				tournament2 = PastDataToCrr.stream().filter(tour->tour.getPlayerId() == player2.getPlayerId()).findAny().orElse(null);
			}
			
			if(match.getSetup().getHomeTeamId() == player.getTeamId()) {
				team = match.getSetup().getHomeTeam();
			} else if(match.getSetup().getAwayTeamId() == player.getTeamId()) {
				team = match.getSetup().getAwayTeam();
			}
			if(valueToProcess.split(",")[2].equalsIgnoreCase("ISPL S1") || valueToProcess.split(",")[2].equalsIgnoreCase("ISPL S2")) {
				statsType = cricketService.getAllStatsType().stream().filter(stype -> stype.getStats_short_name().equalsIgnoreCase(valueToProcess.split(",")[2])).findAny().orElse(null);
				statsType2 = cricketService.getAllStatsType().stream().filter(stype -> stype.getStats_short_name().equalsIgnoreCase(valueToProcess.split(",")[2])).findAny().orElse(null);
				if(statsType == null) {
					return "PopulateL3rdPlayerProfile: Stats Type not found for profile [" + valueToProcess.split(",")[2] + "]";
				}
				stat = cricketService.getAllStats().stream().filter(st -> st.getPlayer_id() == player.getPlayerId() && statsType.getStats_id() == st.getStats_type_id()).findAny().orElse(null);
				if(stat == null) {
					return "populatePlayerProfile: No stats found for player id [" + player.getFull_name() + "] from database is returning NULL";
				}
				stat.setStats_type(statsType);
				
				//for player2
				if(statsType2 == null) {
					return "PopulateL3rdPlayerProfile: Stats Type not found for profile [" + valueToProcess.split(",")[2] + "]";
				}
				stat2 = cricketService.getAllStats().stream().filter(st -> st.getPlayer_id() == player2.getPlayerId() && statsType2.getStats_id() == st.getStats_type_id()).findAny().orElse(null);
				if(stat2 == null) {
					return "populatePlayerProfile: No stats found for player id [" + player2.getFull_name() + "] from database is returning NULL";
				}
				stat2.setStats_type(statsType2);
			}
			if(valueToProcess.split(",")[2].equalsIgnoreCase("ISPL_CAREER")) {
				statsType = new StatsType();
				stat = new Statistics();
				
				Statistics statS1 = CricketFunctions.getStatsByType(Integer.valueOf(valueToProcess.split(",")[0]), "ISPL S1", cricketService.getAllStatsType(), cricketService.getAllStats());
			    Statistics statS2 = CricketFunctions.getStatsByType(Integer.valueOf(valueToProcess.split(",")[0]), "ISPL S2", cricketService.getAllStatsType(), cricketService.getAllStats());
			    
			    if (statS1 == null && statS2 == null) {
			        return "InfoBarPlayerProfile: Stats not found for Player Id [" + valueToProcess.split(",")[0] + "]";
			    }
			    stat = statS1 != null ? statS1 : statS2;
			    stat = CricketFunctions.mergeIsplCareerStats(stat, statS2);
				
				statsType = cricketService.getAllStatsType().stream().filter(st -> st.getStats_short_name().equalsIgnoreCase("D10")).findAny().orElse(null);
				stat.setStats_type(statsType);
				
				//System.out.println(stat.getMatches());
				stat = CricketFunctions.updateTournamentWithH2h(stat, headToHead.getH2hPlayer(), match, CricketUtil.FULL);
				stat = CricketFunctions.updateStatisticsWithMatchData(stat, match, CricketUtil.FULL);
				
				//2
				statsType2 = new StatsType();
				stat2 = new Statistics();
				
				Statistics statSE1 = CricketFunctions.getStatsByType(Integer.valueOf(valueToProcess.split(",")[1]), "ISPL S1", cricketService.getAllStatsType(), cricketService.getAllStats());
			    Statistics statSE2 = CricketFunctions.getStatsByType(Integer.valueOf(valueToProcess.split(",")[1]), "ISPL S2", cricketService.getAllStatsType(), cricketService.getAllStats());
			    
			    if (statSE1 == null && statSE2 == null) {
			        return "InfoBarPlayerProfile: Stats not found for Player Id [" + valueToProcess.split(",")[1] + "]";
			    }
			    stat2 = statSE1 != null ? statSE1 : statSE2;
			    stat2 = CricketFunctions.mergeIsplCareerStats(stat2, statSE2);
				
				statsType2 = cricketService.getAllStatsType().stream().filter(st -> st.getStats_short_name().equalsIgnoreCase("D10")).findAny().orElse(null);
				stat2.setStats_type(statsType2);
				
				//System.out.println(stat2.getMatches());
				stat2 = CricketFunctions.updateTournamentWithH2h(stat2, headToHead.getH2hPlayer(), match, CricketUtil.FULL);
				stat2 = CricketFunctions.updateStatisticsWithMatchData(stat2, match, CricketUtil.FULL);
			}
			
			if(valueToProcess.split(",")[2].equalsIgnoreCase("THIS SERIES")) {
				for(int i=0;i<=top_Batsman.size()-1;i++) {
					if(top_Batsman.get(i).getPlayerId() == player.getPlayerId()) {
						if(top_Batsman.get(i).getBestEquation() % 2 == 0) {
							if(top_Batsman.get(i).getBestEquation() / 2  == 0) {
								best = "-";
							}else {
								best = String.valueOf(top_Batsman.get(i).getBestEquation() /2);
							}
						}else {
							best = (top_Batsman.get(i).getBestEquation()-1) / 2 + "*";
						}
						break;
					}else {
						best = "-";
					}
					
					//2
					if(top_Batsman.get(i).getPlayerId() == player2.getPlayerId()) {
						if(top_Batsman.get(i).getBestEquation() % 2 == 0) {
							if(top_Batsman.get(i).getBestEquation() / 2  == 0) {
								best2 = "-";
							}else {
								best2 = String.valueOf(top_Batsman.get(i).getBestEquation() /2);
							}
						}else {
							best2 = (top_Batsman.get(i).getBestEquation()-1) / 2 + "*";
						}
						break;
					}else {
						best2 = "-";
					}
				}
			}else {
				if(stat.getRuns_conceded() == 0 || stat.getWickets() == 0) {
					Data = "-";
				}else {
					average = stat.getRuns_conceded()/stat.getWickets();
					DecimalFormat df_bo = new DecimalFormat("0.00");
					Data = df_bo.format(average);
				}
				if(stat.getFifties() == 0) {
					fifty = "-";
				}else {
					fifty = String.valueOf(stat.getFifties());
				}
				
				if(stat.getThirties() == 0) {
					thirty = "-";
				}else {
					thirty = String.valueOf(stat.getThirties());
				}
				
				if(stat.getHundreds() == 0) {
					hundred = "-";
				}else {
					hundred = String.valueOf(stat.getHundreds());
				}
				
				if(fifty.equalsIgnoreCase("-") && !hundred.equalsIgnoreCase("-")) {
					fifty = "0";
				}
				
				if(hundred.equalsIgnoreCase("-") && !fifty.equalsIgnoreCase("-")) {
					hundred = "0";
				}
				
				if(CricketFunctions.generateStrikeRate(stat.getRuns(), stat.getBalls_faced(), 1).trim().isEmpty()) {
					strikeRate = "-";
				}else {
					strikeRate = String.valueOf(CricketFunctions.generateStrikeRate(stat.getRuns(), stat.getBalls_faced(), 0));
				}
				
				if(stat.getRuns() == 0) {
					runs = "-";
				}else {
					runs = String.format("%,d\n", stat.getRuns());
				}
				
				if(CricketFunctions.getAverage(stat.getInnings(), stat.getNot_out(), stat.getRuns(), 2, "-").equalsIgnoreCase("0.00")) {
					batAverage = "-";
				}else {
					batAverage = CricketFunctions.getAverage(stat.getInnings(), stat.getNot_out(), stat.getRuns(), 2, "-");
				}
				
				if(player.getSurname() == null) {
					surName = "";
				}else {
					surName = player.getSurname();
				}
				
				if(stat.getBest_score().equalsIgnoreCase("0")) {
					best = "-";
				}else {
					best = stat.getBest_score();
				}
				
				//2
				if(stat2.getRuns_conceded() == 0 || stat2.getWickets() == 0) {
					Data2 = "-";
				}else {
					average2 = stat2.getRuns_conceded()/stat2.getWickets();
					DecimalFormat df_bo = new DecimalFormat("0.00");
					Data2 = df_bo.format(average2);
				}
				
				
				if(CricketFunctions.generateStrikeRate(stat2.getRuns(), stat2.getBalls_faced(), 1).trim().isEmpty()) {
					strikeRate2 = "-";
				}else {
					strikeRate2 = String.valueOf(CricketFunctions.generateStrikeRate(stat2.getRuns(), stat2.getBalls_faced(), 0));
				}
				
				if(stat2.getRuns() == 0) {
					runs2 = "-";
				}else {
					runs2 = String.format("%,d\n", stat2.getRuns());
				}
				
				if(stat2.getBest_score().equalsIgnoreCase("0")) {
					best2 = "-";
				}else {
					best2 = stat2.getBest_score();
				}

			}
			
			if(valueToProcess.split(",")[3].equalsIgnoreCase("AR")) {
				DoadWriteToTrio(print_writer, "read_template OpenerProfile");
			}else {
				DoadWriteToTrio(print_writer, "read_template OpenerProfile_Drone");
			}
			
			populateDoubleProfile(print_writer, match);
			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[4]);
			break;	
		case "POPULATE_GRAPHICS_BALLPROFILE":
			
			debut3 = "NO";
			for(Player ply: cricketService.getAllPlayer()) {
				if(ply.getPlayerId() == Integer.valueOf(valueToProcess.split(",")[0])) {
					if(ply.getDebut().equalsIgnoreCase("YES")) {
						debut3 = "YES";
						player = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(valueToProcess.split(",")[0]), match);
						if(match.getSetup().getHomeTeamId() == player.getTeamId()) {
							team = match.getSetup().getHomeTeam();
						} else if(match.getSetup().getAwayTeamId() == player.getTeamId()) {
							team = match.getSetup().getAwayTeam();
						}
					}else {
						//System.out.println("whattoprocess" + valueToProcess);
						if(valueToProcess.split(",")[1].equalsIgnoreCase("ISPL_CAREER")) {
							profile = "ISPL CAREER";
						}else if(valueToProcess.split(",")[1].equalsIgnoreCase("ISPL S1")){
							profile = "ISPL SEASON 1";
						}else if(valueToProcess.split(",")[1].equalsIgnoreCase("ISPL S2")){
							profile = "ISPL SEASON 2";
						}else if(valueToProcess.split(",")[1].equalsIgnoreCase("THIS SERIES")){
							profile = "ISPL SEASON 3";
							
							PastDataToCrr = CricketFunctions.extractTournamentData("CURRENT_MATCH_DATA", false,  headToHead.getH2hPlayer(), cricketService, match, pasttornament);
							
							top_Batsman = new ArrayList<BestStats>();
							topbowler = new ArrayList<BestStats>();
							for(Tournament tour: PastDataToCrr) {
								for(BestStats bs:tour.getBatsman_best_Stats()) {
									top_Batsman.add(bs);
								}
								for(BestStats bfig: tour.getBowler_best_Stats()) {
									topbowler.add(bfig);
								}
							}
							//System.out.println(PastDataToCrr.size());
							Collections.sort(top_Batsman,new CricketFunctions.PlayerBestStatsComparator());
							Collections.sort(topbowler,new CricketFunctions.PlayerBestStatsComparator());
							
							tournament = PastDataToCrr.stream().filter(tour->tour.getPlayerId() == player.getPlayerId()).findAny().orElse(null);
						}
						
						player = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(valueToProcess.split(",")[0]), match);
						if(match.getSetup().getHomeTeamId() == player.getTeamId()) {
							team = match.getSetup().getHomeTeam();
						} else if(match.getSetup().getAwayTeamId() == player.getTeamId()) {
							team = match.getSetup().getAwayTeam();
						}
						if(valueToProcess.split(",")[1].equalsIgnoreCase("ISPL S1") || valueToProcess.split(",")[1].equalsIgnoreCase("ISPL S2")) {
							statsType = cricketService.getAllStatsType().stream().filter(stype -> stype.getStats_short_name().equalsIgnoreCase(valueToProcess.split(",")[1])).findAny().orElse(null);
							if(statsType == null) {
								return "PopulateL3rdPlayerProfile: Stats Type not found for profile [" + valueToProcess.split(",")[1] + "]";
							}
							stat = cricketService.getAllStats().stream().filter(st -> st.getPlayer_id() == player.getPlayerId() && statsType.getStats_id() == st.getStats_type_id()).findAny().orElse(null);
							if(stat == null) {
								return "populatePlayerProfile: No stats found for player id [" + player.getFull_name() + "] from database is returning NULL";
							}
							stat.setStats_type(statsType);
						}
						if(valueToProcess.split(",")[1].equalsIgnoreCase("ISPL_CAREER")) {
							statsType = new StatsType();
							stat = new Statistics();
							
							Statistics statS1 = CricketFunctions.getStatsByType(Integer.valueOf(valueToProcess.split(",")[0]), "ISPL S1", cricketService.getAllStatsType(), cricketService.getAllStats());
						    Statistics statS2 = CricketFunctions.getStatsByType(Integer.valueOf(valueToProcess.split(",")[0]), "ISPL S2", cricketService.getAllStatsType(), cricketService.getAllStats());
						    
						    if (statS1 == null && statS2 == null) {
						        return "InfoBarPlayerProfile: Stats not found for Player Id [" + valueToProcess.split(",")[0] + "]";
						    }
						    stat = statS1 != null ? statS1 : statS2;
						    stat = CricketFunctions.mergeIsplCareerStats(stat, statS2);
							
							statsType = cricketService.getAllStatsType().stream().filter(st -> st.getStats_short_name().equalsIgnoreCase("D10")).findAny().orElse(null);
							stat.setStats_type(statsType);
							
							//System.out.println(stat.getMatches());
							stat = CricketFunctions.updateTournamentWithH2h(stat, headToHead.getH2hPlayer(), match, CricketUtil.FULL);
							stat = CricketFunctions.updateStatisticsWithMatchData(stat, match, CricketUtil.FULL);
						}
						
						
						
						if(valueToProcess.split(",")[1].equalsIgnoreCase("THIS SERIES")) {
							for(int i=0;i<=topbowler.size()-1;i++) {
								if(topbowler.get(i).getPlayerId() == player.getPlayerId()) {
									if(topbowler.get(i).getBestEquation() % 1000 > 0) {
										best = ((topbowler.get(i).getBestEquation() / 1000) +1 ) + "-" + (1000-(topbowler.get(i).getBestEquation() % 1000));
									}else if(topbowler.get(i).getBestEquation() % 1000 < 0) {
										best = (topbowler.get(i).getBestEquation() / 1000) + "-" + Math.abs(topbowler.get(i).getBestEquation());
									}
									break;
								}else if(topbowler.get(i).getPlayerId() != player.getPlayerId()){
									best = "-";
								}
							}
						}else {
							if(CricketFunctions.getEconomy(stat.getRuns_conceded(), stat.getBalls_bowled(),2,"-").equalsIgnoreCase("0.00")) {
								economy = "-";
							}else {
								economy = CricketFunctions.getEconomy(stat.getRuns_conceded(), stat.getBalls_bowled(),2,"-");
							}
							
							if(stat.getFifties() == 0) {
								fifty = "-";
							}else {
								fifty = String.valueOf(stat.getFifties());
							}
							
							if(stat.getThirties() == 0) {
								thirty = "-";
							}else {
								thirty = String.valueOf(stat.getThirties());
							}
							
							if(stat.getHundreds() == 0) {
								hundred = "-";
							}else {
								hundred = String.valueOf(stat.getHundreds());
							}
							
							if(fifty.equalsIgnoreCase("-") && !hundred.equalsIgnoreCase("-")) {
								fifty = "0";
							}
							
							if(hundred.equalsIgnoreCase("-") && !fifty.equalsIgnoreCase("-")) {
								hundred = "0";
							}
							
							if(CricketFunctions.generateStrikeRate(stat.getRuns(), stat.getBalls_faced(), 1).trim().isEmpty()) {
								strikeRate = "-";
							}else {
								strikeRate = String.valueOf(CricketFunctions.generateStrikeRate(stat.getRuns(), stat.getBalls_faced(), 0));
							}
							
							if(player.getSurname() == null) {
								surName = "";
							}else {
								surName = player.getSurname();
							}
							
							if(stat.getBest_figures().equalsIgnoreCase("0")) {
								best = "-";
							}else {
								best = stat.getBest_figures();
							}
						}
					}
				}
			}
			if(valueToProcess.split(",")[2].equalsIgnoreCase("AR")) {
				DoadWriteToTrio(print_writer, "read_template Profile");
			}else {
				DoadWriteToTrio(print_writer, "read_template Profile_Drone");
			}
			populateBallProfile(print_writer, match,Integer.valueOf(valueToProcess.split(",")[4]));
			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[3]);
			break;	
			
		case "POPULATE_GRAPHICS_AUCTIONBALLPROFILE":
			
			debut3 = "NO";
			for(Player ply: cricketService.getAllPlayer()) {
				if(ply.getPlayerId() == Integer.valueOf(valueToProcess.split(",")[0])) {
					if(ply.getDebut().equalsIgnoreCase("YES")) {
						debut3 = "YES";
						player = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(valueToProcess.split(",")[0]), match);
						if(match.getSetup().getHomeTeamId() == player.getTeamId()) {
							team = match.getSetup().getHomeTeam();
						} else if(match.getSetup().getAwayTeamId() == player.getTeamId()) {
							team = match.getSetup().getAwayTeam();
						}
					}else {
						//System.out.println("whattoprocess" + valueToProcess);
						if(valueToProcess.split(",")[1].equalsIgnoreCase("ISPL_CAREER")) {
							profile = "ISPL CAREER";
						}else if(valueToProcess.split(",")[1].equalsIgnoreCase("ISPL S1")){
							profile = "ISPL SEASON 1";
						}else if(valueToProcess.split(",")[1].equalsIgnoreCase("ISPL S2")){
							profile = "ISPL SEASON 2";
						}else if(valueToProcess.split(",")[1].equalsIgnoreCase("THIS SERIES")){
							profile = "ISPL SEASON 3";
							
							PastDataToCrr = CricketFunctions.extractTournamentData("CURRENT_MATCH_DATA", false,  headToHead.getH2hPlayer(), cricketService, match, pasttornament);
							
							top_Batsman = new ArrayList<BestStats>();
							topbowler = new ArrayList<BestStats>();
							for(Tournament tour: PastDataToCrr) {
								for(BestStats bs:tour.getBatsman_best_Stats()) {
									top_Batsman.add(bs);
								}
								for(BestStats bfig: tour.getBowler_best_Stats()) {
									topbowler.add(bfig);
								}
							}
							//System.out.println(PastDataToCrr.size());
							Collections.sort(top_Batsman,new CricketFunctions.PlayerBestStatsComparator());
							Collections.sort(topbowler,new CricketFunctions.PlayerBestStatsComparator());
							
							tournament = PastDataToCrr.stream().filter(tour->tour.getPlayerId() == player.getPlayerId()).findAny().orElse(null);
						}
						
						player = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(valueToProcess.split(",")[0]), match);
						if(match.getSetup().getHomeTeamId() == player.getTeamId()) {
							team = match.getSetup().getHomeTeam();
						} else if(match.getSetup().getAwayTeamId() == player.getTeamId()) {
							team = match.getSetup().getAwayTeam();
						}
						if(valueToProcess.split(",")[1].equalsIgnoreCase("ISPL S1") || valueToProcess.split(",")[1].equalsIgnoreCase("ISPL S2")) {
							statsType = cricketService.getAllStatsType().stream().filter(stype -> stype.getStats_short_name().equalsIgnoreCase(valueToProcess.split(",")[1])).findAny().orElse(null);
							if(statsType == null) {
								return "PopulateL3rdPlayerProfile: Stats Type not found for profile [" + valueToProcess.split(",")[1] + "]";
							}
							stat = cricketService.getAllStats().stream().filter(st -> st.getPlayer_id() == player.getPlayerId() && statsType.getStats_id() == st.getStats_type_id()).findAny().orElse(null);
							if(stat == null) {
								return "populatePlayerProfile: No stats found for player id [" + player.getFull_name() + "] from database is returning NULL";
							}
							stat.setStats_type(statsType);
						}
						if(valueToProcess.split(",")[1].equalsIgnoreCase("ISPL_CAREER")) {
							statsType = new StatsType();
							stat = new Statistics();
							
							Statistics statS1 = CricketFunctions.getStatsByType(Integer.valueOf(valueToProcess.split(",")[0]), "ISPL S1", cricketService.getAllStatsType(), cricketService.getAllStats());
						    Statistics statS2 = CricketFunctions.getStatsByType(Integer.valueOf(valueToProcess.split(",")[0]), "ISPL S2", cricketService.getAllStatsType(), cricketService.getAllStats());
						    
						    if (statS1 == null && statS2 == null) {
						        return "InfoBarPlayerProfile: Stats not found for Player Id [" + valueToProcess.split(",")[0] + "]";
						    }
						    stat = statS1 != null ? statS1 : statS2;
						    stat = CricketFunctions.mergeIsplCareerStats(stat, statS2);
							
							statsType = cricketService.getAllStatsType().stream().filter(st -> st.getStats_short_name().equalsIgnoreCase("D10")).findAny().orElse(null);
							stat.setStats_type(statsType);
							
							//System.out.println(stat.getMatches());
							stat = CricketFunctions.updateTournamentWithH2h(stat, headToHead.getH2hPlayer(), match, CricketUtil.FULL);
							stat = CricketFunctions.updateStatisticsWithMatchData(stat, match, CricketUtil.FULL);
						}
						
						
						
						if(valueToProcess.split(",")[1].equalsIgnoreCase("THIS SERIES")) {
							for(int i=0;i<=topbowler.size()-1;i++) {
								if(topbowler.get(i).getPlayerId() == player.getPlayerId()) {
									if(topbowler.get(i).getBestEquation() % 1000 > 0) {
										best = ((topbowler.get(i).getBestEquation() / 1000) +1 ) + "-" + (1000-(topbowler.get(i).getBestEquation() % 1000));
									}else if(topbowler.get(i).getBestEquation() % 1000 < 0) {
										best = (topbowler.get(i).getBestEquation() / 1000) + "-" + Math.abs(topbowler.get(i).getBestEquation());
									}
									break;
								}else if(topbowler.get(i).getPlayerId() != player.getPlayerId()){
									best = "-";
								}
							}
						}else {
							if(CricketFunctions.getEconomy(stat.getRuns_conceded(), stat.getBalls_bowled(),2,"-").equalsIgnoreCase("0.00")) {
								economy = "-";
							}else {
								economy = CricketFunctions.getEconomy(stat.getRuns_conceded(), stat.getBalls_bowled(),2,"-");
							}
							
							if(stat.getFifties() == 0) {
								fifty = "-";
							}else {
								fifty = String.valueOf(stat.getFifties());
							}
							
							if(stat.getThirties() == 0) {
								thirty = "-";
							}else {
								thirty = String.valueOf(stat.getThirties());
							}
							
							if(stat.getHundreds() == 0) {
								hundred = "-";
							}else {
								hundred = String.valueOf(stat.getHundreds());
							}
							
							if(fifty.equalsIgnoreCase("-") && !hundred.equalsIgnoreCase("-")) {
								fifty = "0";
							}
							
							if(hundred.equalsIgnoreCase("-") && !fifty.equalsIgnoreCase("-")) {
								hundred = "0";
							}
							
							if(CricketFunctions.generateStrikeRate(stat.getRuns(), stat.getBalls_faced(), 1).trim().isEmpty()) {
								strikeRate = "-";
							}else {
								strikeRate = String.valueOf(CricketFunctions.generateStrikeRate(stat.getRuns(), stat.getBalls_faced(), 0));
							}
							
							if(player.getSurname() == null) {
								surName = "";
							}else {
								surName = player.getSurname();
							}
							
							if(stat.getBest_figures().equalsIgnoreCase("0")) {
								best = "-";
							}else {
								best = stat.getBest_figures();
							}
						}
					}
				}
			}
			if(valueToProcess.split(",")[2].equalsIgnoreCase("AR")) {
				DoadWriteToTrio(print_writer, "read_template Profile_ActionImage");
			}else {
				DoadWriteToTrio(print_writer, "read_template Profile_SctionImageDrone");
			}
			populateBallProfileAuction(print_writer, match,Integer.valueOf(valueToProcess.split(",")[4]));
			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[3]);
			break;
		case"POPULATE_GRAPHICS_FIXTURE":
			DoadWriteToTrio(print_writer, "read_template FF_Team_Schedule");
			//System.out.println("valueToProcess = " + valueToProcess);
			populateFixture(print_writer, match, cricketService, Integer.valueOf(valueToProcess.split(",")[0]));
			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[1]);
			break;	
//		case"POPULATE_GRAPHICS_NEXT_TO_BAT":
//			DoadWriteToTrio(print_writer, "read_template Next3");
//			populateNextToBat(print_writer,match, cricketService,category);
//			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[0]);
//			break;
		case "POPULATE_GRAPHICS_NEXT_TO_BAT":
		    sendVizMSENextToBatPage(valueToProcess.split(",")[0], match, cricketService, category, ipAddress, showname);
		    break;	
		case "POPULATE_GRAPHICS_LINEUP":	
			DoadWriteToTrio(print_writer, "read_template LineUp_2Teams_WithChangeOns");
			populateLineUp(print_writer,match, cricketService,Integer.valueOf(valueToProcess.split(",")[0]));
			DoadWriteToTrio(print_writer, "saveas " + valueToProcess.split(",")[1]);
			break;
		case "POPULATE_GRAPHICS":
			Map<String, String> mp = IndexController.getDataFromExcelFile(mainCricketDirectory);
			String [] str =mp.get(valueToProcess).split("\n");
			
			if((str.length-5)<Integer.valueOf(str[1].split("X")[0])) {
				return " Something wrong with your excel at "+ valueToProcess;					
			}else {
				DoadWriteToTrio(print_writer, "read_template 9020-Team_Single");
				PopulateGraphics(print_writer,str);
			}
			break;
		}
		
		return null;
	}
	private void popualteIspl_MATCH_SUMMARY(PrintWriter print_writer, MatchAllData previous_match, CricketService cricketService) throws Exception {		
		//HEADER
		List<String> inningData  = new ArrayList<>();
		
		inningData.add("tabfield:set_value_no_update 001-TEAM-REF-NAME-1 " +  (previous_match.getSetup().getHomeTeamId()== 
				previous_match.getMatch().getInning().get(0).getBattingTeamId()? 
						previous_match.getSetup().getHomeTeam().getTeamName1():previous_match.getSetup().getAwayTeam().getTeamName1()));
		 inningData.add("tabfield:set_value_no_update 002-TEAM-REF-NAME-2 " +  (previous_match.getSetup().getHomeTeamId()== previous_match.getMatch().getInning().get(1).getBattingTeamId()? previous_match.getSetup().getHomeTeam().getTeamName1():previous_match.getSetup().getAwayTeam().getTeamName1()));
		 int rowId =0;
		 int numberOfRows = 4;
		 String dls_data = "";
		
		for(int i = 1; i <= 2 ; i++) {
			if(i == 1) {
				rowId=0;
			}else {
				rowId=0;
			}
			
			inningData.add("tabfield:set_value_no_update 201-TEAM-FIRST-NAME " +  
					previous_match.getMatch().getInning().get(i-1).getBatting_team().getTeamName2());
			inningData.add("tabfield:set_value_no_update 001-TEAM-REF-NAME-" + i + " " +  
					previous_match.getMatch().getInning().get(i-1).getBatting_team().getTeamName3());
			
			if(previous_match.getMatch().getInning().get(i-1).getBattingTeamId() == previous_match.getSetup().getTossWinningTeam()) {
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$All_Graphics$Side" + WhichSide + "$Summary$" + containerName + i + 
//						"$Title$Toss$select_Toss*FUNCTION*Omo*vis_con SET " + (config.getCategory().equalsIgnoreCase("MEN")?1:2) + "\0", print_writers);
//			
				
			
			
			}else {
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$All_Graphics$Side" + WhichSide + "$Summary$" + containerName + i + 
//						"$Title$Toss$select_Toss*FUNCTION*Omo*vis_con SET 0\0", print_writers);
			}
			
			if(i==1) {
				
				dls_data = (previous_match.getSetup().getReducedOvers() != null && !previous_match.getSetup().getReducedOvers().isEmpty() ? "(" + previous_match.getSetup().getReducedOvers() + ")":"");
				//CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$All_Graphics$Side" + WhichSide + "$Summary$"+ containerName +"2*ACTIVE SET 0 \0", print_writers);
			}else if(i==2) {
				dls_data = (previous_match.getSetup().getTargetOvers() != null && !previous_match.getSetup().getTargetOvers().trim().isEmpty() ? "(" + previous_match.getSetup().getTargetOvers() + ")":"");
				
			//	CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$All_Graphics$Side" + WhichSide + "$Summary$"+ containerName +"2*ACTIVE SET 1 \0", print_writers);
			}
			
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$All_Graphics$Side" + WhichSide + "$Summary$" + containerName + i + 
//					"$Title$txt_OversHead*GEOM*TEXT SET " + CricketFunctions.OverBalls(previous_match.getMatch().getInning().get(i-1).getTotalOvers(), 
//							previous_match.getMatch().getInning().get(i-1).getTotalBalls()) + dls_data + "\0", print_writers);
			
			if(previous_match.getMatch().getInning().get(i-1).getBattingCard() != null) {
				Collections.sort(previous_match.getMatch().getInning().get(i-1).getBattingCard(),new CricketFunctions.BatsmenScoreComparator());
				
				for(BattingCard bc : previous_match.getMatch().getInning().get(i-1).getBattingCard()) {
					if(bc.getRuns() > 0) {
						rowId++;
						
						
						DoadWriteToTrio(print_writer, "table:set_cell_value" + "2" + "07_INNINGS-DATA 0" + i + "-BATTER-NAME" + rowId 
								+ bc.getPlayer().getTicker_name());
						
						
						if(!CricketFunctions.checkBatAndBallImpactInOutPlayer(previous_match.getEventFile().getEvents(), bc.getPlayerId()).isEmpty()) {
							switch(CricketFunctions.checkBatAndBallImpactInOutPlayer(previous_match.getEventFile().getEvents(), bc.getPlayerId())) {
							case "IMP_IN":
								DoadWriteToTrio(print_writer, "table:set_cell_value 207_INNINGS-DATA 0" + i + "-BATTER-NAME" + rowId 
										+ 2);
								break;
							case "IMP_OUT":
								DoadWriteToTrio(print_writer, "table:set_cell_value 207_INNINGS-DATA 0" + i + "-BATTER-NAME" + rowId 
										+ 2);
								break;
							case "CON_IN":
								DoadWriteToTrio(print_writer, "table:set_cell_value 207_INNINGS-DATA 0" + i + "-BATTER-NAME" + rowId 
										+ 4);
								break;
							case "CON_OUT":
								DoadWriteToTrio(print_writer, "table:set_cell_value 207_INNINGS-DATA 0" + i + "-BATTER-NAME" + rowId 
										+ 1);
								break;
							}
						}else {
							DoadWriteToTrio(print_writer, "table:set_cell_value 207_INNINGS-DATA 0" + i + "-BATTER-NAME" + rowId 
									+ 0);
						}
						
						DoadWriteToTrio(print_writer, "table:set_cell_value 207_INNINGS-DATA 03-RUNS" + rowId 
								+ bc.getRuns());
						
						DoadWriteToTrio(print_writer, "table:set_cell_value 207_INNINGS-DATA 05-BALLS" + rowId 
								+ bc.getRuns());
						
						DoadWriteToTrio(print_writer, "table:set_cell_value 207_INNINGS-DATA 04-STAR" + rowId 
								+ (bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)?"*":""));
						
						if(i == 1 && rowId >= numberOfRows) {
							break;
						}else if(i == 2 && rowId >= numberOfRows) {
							break;
						}
					}
				}
			}
			for(int j = rowId+1; j <= numberOfRows; j++) {
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$All_Graphics$Side" + WhichSide + "$Summary$" + containerName 
//						+ i + "$Row" + j + "$Batter*ACTIVE SET 0\0", print_writers);
			}
			
			if(i == 1) {
				rowId = 0;
			}
			else {
				rowId = 0;
			}
			if(previous_match.getMatch().getInning().get(i-1).getBowlingCard() != null) {
				Collections.sort(previous_match.getMatch().getInning().get(i-1).getBowlingCard(),new CricketFunctions.BowlerFiguresComparator());
				for(BowlingCard boc : previous_match.getMatch().getInning().get(i-1).getBowlingCard()) {
					
					if(boc.getWickets() > 0) {
						rowId++;
						
						if(!CricketFunctions.checkBatAndBallImpactInOutPlayer(previous_match.getEventFile().getEvents(), boc.getPlayerId()).isEmpty()) {
							switch(CricketFunctions.checkBatAndBallImpactInOutPlayer(previous_match.getEventFile().getEvents(), boc.getPlayerId())) {
							case "IMP_IN":
								DoadWriteToTrio(print_writer, "table:set_cell_value 207_INNINGS-DATA 0" + i + "-BATTER-NAME" + rowId 
										+ 2);
								break;
							case "IMP_OUT":
								DoadWriteToTrio(print_writer, "table:set_cell_value 207_INNINGS-DATA 0" + i + "-BATTER-NAME" + rowId 
										+ 2);
								break;
							case "CON_IN":
								DoadWriteToTrio(print_writer, "table:set_cell_value 207_INNINGS-DATA 0" + i + "-BATTER-NAME" + rowId 
										+ 2);
								break;
							case "CON_OUT":
								DoadWriteToTrio(print_writer, "table:set_cell_value 207_INNINGS-DATA 0" + i + "-BATTER-NAME" + rowId 
										+ 2);
								break;
							}
						}else {
							DoadWriteToTrio(print_writer, "table:set_cell_value 207_INNINGS-DATA 0" + i + "-BATTER-NAME" + rowId 
									+ 2);
						}
						
						DoadWriteToTrio(print_writer, "table:set_cell_value" + "2" + "07_INNINGS-DATA 0" + i + "-BATTER-NAME" + rowId 
								+ boc.getPlayer().getTicker_name());
						DoadWriteToTrio(print_writer, "table:set_cell_value" + "2" + "07_INNINGS-DATA 0" + i + "-BATTER-NAME" + rowId 
								+ boc.getWickets());
						DoadWriteToTrio(print_writer, "table:set_cell_value" + "2" + "07_INNINGS-DATA 0" + i + "-BATTER-NAME" + rowId 
								+  CricketFunctions.OverBalls(boc.getOvers(),boc.getBalls()));
						DoadWriteToTrio(print_writer, "table:set_cell_value" + "2" + "07_INNINGS-DATA 0" + i + "-BATTER-NAME" + rowId 
								+ CricketFunctions.OverBalls(boc.getOvers(),boc.getBalls()));
						
			
						if(i == 1 && rowId >= numberOfRows) {
							break;
						}
						else if(i == 2 && rowId >= numberOfRows) {
							break;
						}
					}
				}
			}
			for(int j = rowId+1; j <= numberOfRows; j++) {
				//CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$All_Graphics$Side" + WhichSide + "$Summary$" + 
				//		containerName + i + "$Row" + j + "$Bowler*ACTIVE SET 0\0", print_writers);
			}
			
		}
		
//		 inningData.add("tabfield:set_value_no_update 001-TEAM-REF-NAME-1 " +  (previous_match.getSetup().getHomeTeamId()== previous_match.getMatch().getInning().get(0).getBattingTeamId()? previous_match.getSetup().getHomeTeam().getTeamName1():previous_match.getSetup().getAwayTeam().getTeamName1()));
//		 inningData.add("tabfield:set_value_no_update 002-TEAM-REF-NAME-2 " +  (previous_match.getSetup().getHomeTeamId()== previous_match.getMatch().getInning().get(1).getBattingTeamId()? previous_match.getSetup().getHomeTeam().getTeamName1():previous_match.getSetup().getAwayTeam().getTeamName1()));
//		 inningData.add("tabfield:set_value_no_update 005-TEAM-NAME " + (previous_match.getSetup().getHomeTeamId()== previous_match.getMatch().getInning().get(0).getBattingTeamId()? previous_match.getSetup().getHomeTeam().getTeamName1():previous_match.getSetup().getAwayTeam().getTeamName1()));
//		 inningData.add("tabfield:set_value_no_update 012-TEAM-NAME " + (previous_match.getSetup().getHomeTeamId()== previous_match.getMatch().getInning().get(1).getBattingTeamId()? previous_match.getSetup().getHomeTeam().getTeamName1():previous_match.getSetup().getAwayTeam().getTeamName1()));
//		 inningData.add("tabfield:set_value_no_update 003-HEADER " + "SUMMARY");
//		 inningData.add("tabfield:set_value_no_update 004-SUB-HEADER " + previous_match.getSetup().getTournament() + " - " + previous_match.getSetup().getMatchIdent());
//
//		
//		//BODY
//		 inningData.add("tabfield:set_value_no_update 005-OVERS-VALUE " + CricketFunctions.OverBalls(previous_match.getMatch().getInning().get(0).getTotalOvers(),previous_match.getMatch().getInning().get(0).getTotalBalls()));
//		 inningData.add("tabfield:set_value_no_update 005-SCORE-VALUE " + CricketFunctions.getTeamScore(previous_match.getMatch().getInning().get(0),"-", false));
//		 inningData.add("tabfield:set_value_no_update 012-OVERS-VALUE " + CricketFunctions.OverBalls(previous_match.getMatch().getInning().get(1).getTotalOvers(),previous_match.getMatch().getInning().get(1).getTotalBalls()));
//		 inningData.add("tabfield:set_value_no_update 012-SCORE-VALUE " + CricketFunctions.getTeamScore(previous_match.getMatch().getInning().get(1),"-", false));
//		for(int i = 1; i <= 2 ; i++) {
//			inningData.add("tabfield:set_value_no_update "+(i==1?"008":"012")+"-SELECT-CHALLENGE 0");
//			String tapeData = getBowlerRunsOverbyOver(i, previous_match.getEventFile().getEvents(), previous_match);
//			if(i == 1) {
//				if(previous_match.getSetup().getTargetOvers() != null && !previous_match.getSetup().getTargetOvers().trim().isEmpty()) {
//					inningData.add("tabfield:set_value_no_update 005-DLS-VALUE " +"(" + previous_match.getSetup().getTargetOvers() + ")");
//				}
//				if(previous_match.getMatch().getInning().get(i-1).getBattingTeamId() == previous_match.getSetup().getTossWinningTeam()) {
//					inningData.add("tabfield:set_value_no_update 005-SELECT-TOSS 1");
//
//				}else {
//					inningData.add("tabfield:set_value_no_update 005-SELECT-TOSS 0");
//				}
//			}else {
//				if(previous_match.getSetup().getTargetOvers() != null && !previous_match.getSetup().getTargetOvers().trim().isEmpty()) {
//					if((Integer.valueOf(previous_match.getSetup().getTargetOvers())*6) != 
//							((previous_match.getMatch().getInning().get(0).getTotalOvers()*6)+previous_match.getMatch().getInning().get(0).getTotalBalls())) {
//						inningData.add("tabfield:set_value_no_update 005-DLS-VALUE " +"(" + previous_match.getSetup().getTargetOvers() + ")");
//
//					}else {
//						inningData.add("tabfield:set_value_no_update 005-DLS-VALUE " +"(" + previous_match.getSetup().getTargetOvers() + ")");
//						inningData.add("tabfield:set_value_no_update 012-DLS-VALUE " +"(" + previous_match.getSetup().getTargetOvers() + ")");
//					}
//				}else {
//					inningData.add("tabfield:set_value_no_update 005-DLS-VALUE " +"");
//					inningData.add("tabfield:set_value_no_update 012-DLS-VALUE " +"");
//	
//				}
//				if(previous_match.getMatch().getInning().get(i-1).getBattingTeamId() == previous_match.getSetup().getTossWinningTeam()) {
//					inningData.add("tabfield:set_value_no_update 012-SELECT-TOSS 1");
//				}else {
//					inningData.add("tabfield:set_value_no_update 012-SELECT-TOSS 0");
//				}
//			}
//			if(previous_match.getMatch().getInning().get(i-1).getBattingCard() != null) {
//				Collections.sort(previous_match.getMatch().getInning().get(i-1).getBattingCard(),new CricketFunctions.BatsmenScoreComparator());
//				int rowId =0;
//				for(BattingCard bc : previous_match.getMatch().getInning().get(i-1).getBattingCard()) {
//					if(rowId >=3) break;
//					if(bc.getRuns() > 0) {
//						rowId++;
//						inningData.add("tabfield:set_value_no_update 0"+(previous_match.getMatch().getInning().get(i-1).getInningNumber()==1?"0":"")+((6*previous_match.getMatch().getInning().get(i-1).getInningNumber())+(rowId-1))
//								+"-BATSMAN-NAME "+ bc.getPlayer().getTicker_name());
//						inningData.add("tabfield:set_value_no_update 0"+(previous_match.getMatch().getInning().get(i-1).getInningNumber()==1?"0":"")+((6*previous_match.getMatch().getInning().get(i-1).getInningNumber())+(rowId-1))
//								+"-BATSMAN-RUNS "+ bc.getRuns()+(bc.getStatus().equalsIgnoreCase("OUT")?"":"* "));
//						inningData.add("tabfield:set_value_no_update 0"+(previous_match.getMatch().getInning().get(i-1).getInningNumber()==1?"0":"")+((6*previous_match.getMatch().getInning().get(i-1).getInningNumber())+(rowId-1))
//								+"-BATSMAN-BALLS "+ bc.getBalls());
//			
//						String photo = "";
//						if(IndexController.session_Configurations.getPrimaryIpAddress().equalsIgnoreCase("LOCALHOST")) {
//							photo = "C:\\\\Images\\\\ISPL\\\\PHOTOS\\"+(bc.getPlayer().getTeamId()==previous_match.getSetup().getHomeTeamId() ?previous_match.getSetup().getHomeTeam().getTeamName4() : 
//								previous_match.getSetup().getAwayTeam().getTeamName4() )+ "\\\\"+bc.getPlayer().getPhoto()+CricketUtil.PNG_EXTENSION;
//						}else {
//							photo = "\\\\\\\\"+IndexController.session_Configurations.getPrimaryIpAddress()+"\\\\\\\\c\\\\\\\\Images\\\\\\\\ISPL\\\\\\\\PHOTOS\\\\\\\\"+
//									(bc.getPlayer().getTeamId()==previous_match.getSetup().getHomeTeamId() ?previous_match.getSetup().getHomeTeam().getTeamName4() : 
//										previous_match.getSetup().getAwayTeam().getTeamName4() )+ "\\\\\\\\"+bc.getPlayer().getPhoto()+CricketUtil.PNG_EXTENSION;
//						}
//						inningData.add("tabfield:set_value_no_update 0"+(previous_match.getMatch().getInning().get(i-1).getInningNumber()==1?"0":"")+((6*previous_match.getMatch().getInning().get(i-1).getInningNumber())+(rowId-1))
//								+"-BATSMAN-IMG "+photo);
//
//					}
//				}
//				ArrayList<Event> tapeBall = new ArrayList<Event>();
//				int log_50_bowlerNo = 0;
//				for(int j= previous_match.getEventFile().getEvents().size()-1;j >= 0 ; j--) {
//					if(previous_match.getEventFile().getEvents().get(j).getEventInningNumber() == previous_match.getMatch().getInning().get(i-1).getInningNumber()) {
//						if(previous_match.getEventFile().getEvents().get(j).getEventType().equalsIgnoreCase(CricketUtil.LOG_50_50)) {
//							log_50_bowlerNo = previous_match.getEventFile().getEvents().get(j).getEventBowlerNo();
//							if(previous_match.getEventFile().getEvents().get(j).getEventExtra().equalsIgnoreCase("-")) {
//								inningData.add("tabfield:set_value_no_update "+(i==1?"008":"014")+"-SELECT-CHALLENGE 1");
//								inningData.add("tabfield:set_value_no_update " + (i == 1 ? "008-NEG-CHALLENGE-VALUE" : "014-CHALLENGE-VALUE") + " "
//										+ previous_match.getEventFile().getEvents().get(j).getEventExtraRuns());
//							}
//							else if(previous_match.getEventFile().getEvents().get(j).getEventExtra().equalsIgnoreCase("+")) {
//								inningData.add("tabfield:set_value_no_update "+(i==1?"008":"014")+"-SELECT-CHALLENGE 2");
//								inningData.add("tabfield:set_value_no_update " + (i == 1 ? "008-POS-CHALLENGE-VALUE" : "014-CHALLENGE-VALUE") + " "
//										+ previous_match.getEventFile().getEvents().get(j).getEventExtraRuns());
//							}
//						}
//						if(previous_match.getEventFile().getEvents().get(j).getEventExtra() != null) {
//							if(previous_match.getEventFile().getEvents().get(j).getEventExtra().equalsIgnoreCase("TAPE")) {
//								tapeBall.add(previous_match.getEventFile().getEvents().get(j));
//							}
//						}
//					}
//				}
//				if(tapeBall.size()>0) {
//					inningData.add("tabfield:set_value_no_update "+(i==1?"011":"017")+"-TAPEBALL-VALUE "+tapeData.split(",")[1]);
//					inningData.add("tabfield:set_value_no_update "+(i==1?"011":"017")+"-TAPEBALL-OVERS "+tapeBall.size());
//				}else {
//					inningData.add("tabfield:set_value_no_update "+(i==1?"011":"017")+"-TAPEBALL-VALUE 0");
//					inningData.add("tabfield:set_value_no_update "+(i==1?"011":"017")+"-TAPEBALL-OVERS 0");
//				}
//				if(previous_match.getMatch().getInning().get(i-1).getBowlingCard() != null) {
//					Collections.sort(previous_match.getMatch().getInning().get(i-1).getBowlingCard(),new CricketFunctions.BowlerFiguresComparator());
//					 rowId = 0;
//					for(BowlingCard boc : previous_match.getMatch().getInning().get(i-1).getBowlingCard()) {
//						if(rowId >=3) break;
//							rowId++;
//							
//							for(Event evnt : tapeBall) {
//								String formattedValue = ((previous_match.getMatch().getInning().get(i - 1).getInningNumber()==1?9:15) + (rowId - 1) > 9) ? 
//				                         "" + ((previous_match.getMatch().getInning().get(i - 1).getInningNumber()==1?9:15) + (rowId - 1)) : 
//				                         "" + ((previous_match.getMatch().getInning().get(i - 1).getInningNumber()==1?9:15) + (rowId - 1));
//
//					
//								if(boc.getPlayerId() == log_50_bowlerNo) {
//									inningData.add("tabfield:set_value_no_update 0"+formattedValue+"-SELECT-OVERTYPE 2");
//									if(tapeBall.get(0).getEventBowlerNo() == boc.getPlayerId()) {
//										inningData.add("tabfield:set_value_no_update 0"+formattedValue+"-SELECT-OVERTYPE 1");
//									}
//									if(tapeBall.size() > 1) {
//										if(tapeBall.get(1).getEventBowlerNo() == boc.getPlayerId()) {
//											inningData.add("tabfield:set_value_no_update 0"+formattedValue+"-SELECT-OVERTYPE 1");
//										}
//									}
//									break;
//								}else {
//									if(evnt.getEventBowlerNo() == boc.getPlayerId()) {
//										inningData.add("tabfield:set_value_no_update 0"+formattedValue+"-SELECT-OVERTYPE 1");
//
//										if(tapeBall.size() == 2) {
//											if(tapeBall.get(0).getEventBowlerNo() == tapeBall.get(1).getEventBowlerNo()) {
//												inningData.add("tabfield:set_value_no_update 0"+formattedValue+"-SELECT-OVERTYPE 1");
//											}
//										}
//										break;
//									}else {
//										inningData.add("tabfield:set_value_no_update 0"+formattedValue+"-SELECT-OVERTYPE 0");
//									}
//								}
//							}
//						String formattedValue = ((previous_match.getMatch().getInning().get(i - 1).getInningNumber()==1?9:15) + (rowId - 1) > 9) ? 
//		                         "" + ((previous_match.getMatch().getInning().get(i - 1).getInningNumber()==1?9:15) + (rowId - 1)) : 
//		                         "0" + ((previous_match.getMatch().getInning().get(i - 1).getInningNumber()==1?9:15) + (rowId - 1));
//						
//						inningData.add("tabfield:set_value_no_update 0" + formattedValue +"-BOWLER-NAME "+ boc.getPlayer().getTicker_name());
//						inningData.add("tabfield:set_value_no_update 0" + formattedValue +"-BOWLER-RUNS "+ (boc.getWickets()+"-" + boc.getRuns()));
//						inningData.add("tabfield:set_value_no_update 0" + formattedValue +"-BOWLER-BALLS "+ CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()));
//			
//						String photo = "";
//						if(IndexController.session_Configurations.getPrimaryIpAddress().equalsIgnoreCase("LOCALHOST")) {
//							photo = "C:\\\\Images\\\\ISPL\\\\PHOTOS\\"+(boc.getPlayer().getTeamId()==previous_match.getSetup().getHomeTeamId() ?previous_match.getSetup().getHomeTeam().getTeamName4() : 
//								previous_match.getSetup().getAwayTeam().getTeamName4() )+ "\\\\"+boc.getPlayer().getPhoto()+CricketUtil.PNG_EXTENSION;
//						}else {
//							photo = "\\\\\\\\"+IndexController.session_Configurations.getPrimaryIpAddress()+"\\\\\\\\c\\\\\\\\Images\\\\\\\\ISPL\\\\\\\\PHOTOS\\\\\\\\"+
//									(boc.getPlayer().getTeamId()==previous_match.getSetup().getHomeTeamId() ?previous_match.getSetup().getHomeTeam().getTeamName4() : 
//										previous_match.getSetup().getAwayTeam().getTeamName4() )+ "\\\\\\\\"+boc.getPlayer().getPhoto()+CricketUtil.PNG_EXTENSION;
//						}
//						inningData.add("tabfield:set_value_no_update 0" + formattedValue + "-BOWLER-IMG "+photo);
//
//					}
//				}
//			}
//		}
//		
//		//FOOTER
//		inningData.add("tabfield:set_value_no_update 018-SELECT-FOOTER 2");
//		inningData.add("tabfield:set_value_no_update 019-FOOTER-TEXT " + CricketFunctions.GenerateMatchSummaryStatus(
//				2, previous_match,CricketUtil.FULL, "", IndexController.session_Configurations.getBroadcaster(), true));
//		
//		for(String str : inningData) {
//			DoadWriteToTrio(print_writer, str);
//		}
	}
	public void PopulateGraphics(PrintWriter print_writer,String[] str) {
		for (int i = 6; i < str.length; i++) {
		    String line[] = splitString(str[i]);
		    for (int j = 0; j < line.length; j++) {
		        String columnName;
		        switch (j) {
		            case 0:
		                columnName = "001-NAME";
		                break;
		            case 1:
		                columnName = "002-CAPTAIN";
		                break;
		            case 2:
		                columnName = "003-ROLE";
		                break;
		            case 3:
		                columnName = "004-STAT1";
		                break;
		            case 4:
		                columnName = "005-STAT2";
		                break;
		            default:
		                columnName = ""; 
		        }

		        DoadWriteToTrio(print_writer, "table:set_cell_value 002_DataALL " + columnName + " " + (i - 6) + " " + line[j]);
		    }
		}

		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-Header01 " + str[3] + " ");
		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  000-Header02 " + " ");
		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  000-Header03 " + " ");
		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  000-SubHeader " + str[4] + " ");
		
		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  001-Title1 " + " ");
		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  002-Title2 " + " ");

		
		DoadWriteToTrio(print_writer, "saveas " + 1001);
		
	 }
	 
	public void popualteIspl50_50(PrintWriter print_writer,int player_id,String cr_value,MatchAllData match, CricketService cricketService) {
		 Player player = CricketFunctions.getPlayerFromMatchData(player_id ,match);
		 String photo = "";
		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 002-TEAM-REF-NAME " +(player.getTeamId()==match.getSetup().getHomeTeamId() ?
				 match.getSetup().getAwayTeam().getTeamName4() : match.getSetup().getHomeTeam().getTeamName4() )+ " ");
		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 005-STAT-VALUE " + cr_value);
		
		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 015-FIRST-NAME " +player.getFirstname());
		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 016-LAST-NAME " +(player.getSurname()==null ? "" : player.getSurname()));
		 
		 if(IndexController.session_Configurations.getPrimaryIpAddress().equalsIgnoreCase("LOCALHOST")) {
				photo = "C:\\\\Images\\\\ISPL\\\\PHOTOS\\"+(player.getTeamId()==match.getSetup().getHomeTeamId() ?match.getSetup().getHomeTeam().getTeamName4() : 
					match.getSetup().getAwayTeam().getTeamName4() )+ "\\\\"+player.getPhoto()+CricketUtil.PNG_EXTENSION;
		}else {
			photo = "\\\\\\\\"+IndexController.session_Configurations.getPrimaryIpAddress()+"\\\\\\\\c\\\\\\\\Images\\\\\\\\ISPL\\\\\\\\PHOTOS\\\\\\\\"+
					(player.getTeamId()==match.getSetup().getHomeTeamId() ?match.getSetup().getHomeTeam().getTeamName4() : 
						match.getSetup().getAwayTeam().getTeamName4() )+ "\\\\\\\\"+player.getPhoto()+CricketUtil.PNG_EXTENSION;
		}
		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 014-PLAYER-IMAGE " +photo);
	 }
	private void popualteIsplTape(PrintWriter print_writer, Integer player_id, MatchAllData match, CricketService cricketService) {
		 Player player = CricketFunctions.getPlayerFromMatchData(player_id ,match);
		 String photo = "";
		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 002-TEAM-REF-NAME " +(player.getTeamId()==match.getSetup().getHomeTeamId() ?
				 match.getSetup().getAwayTeam().getTeamName4() : match.getSetup().getHomeTeam().getTeamName4() )+ " ");		
		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 005-FIRST-NAME " +player.getFirstname());
		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 006-LAST-NAME " +(player.getSurname()==null ? "" : player.getSurname()));
		 
		 if(IndexController.session_Configurations.getPrimaryIpAddress().equalsIgnoreCase("LOCALHOST")) {
				photo = "C:\\\\Images\\\\ISPL\\\\PHOTOS\\"+(player.getTeamId()==match.getSetup().getHomeTeamId() ?match.getSetup().getHomeTeam().getTeamName4() : 
					match.getSetup().getAwayTeam().getTeamName4() )+ "\\\\"+player.getPhoto()+CricketUtil.PNG_EXTENSION;
		}else {
			photo = "\\\\\\\\"+IndexController.session_Configurations.getPrimaryIpAddress()+"\\\\c\\\\Images\\\\ISPL\\\\PHOTOS\\\\"+
					(player.getTeamId()==match.getSetup().getHomeTeamId() ?match.getSetup().getHomeTeam().getTeamName4() : 
						match.getSetup().getAwayTeam().getTeamName4() )+ "\\\\"+player.getPhoto()+CricketUtil.PNG_EXTENSION;
		}
		DoadWriteToTrio(print_writer,  "tabfield:set_value_no_update 004-PLAYER-IMAGE \"" + photo + "\"");
	}
	
	
	
// 	private void populateComparison(PrintWriter print_writer,MatchAllData match,String cat) {
// 		
// 		
// 		for(Inning inn : match.getMatch().getInning()) {
//			if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
//		//		System.out.println("cat.toUpperCase()" + cat.toUpperCase());
//			//	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " + cat.toUpperCase());
//				
////				DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " +
////					    (cat.equalsIgnoreCase("MEN") ? "MENS" : "WOMENS"));
//				
//				DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-TEAMNAME " +  inn.getBowling_team().getTeamBadge());
//
// 			DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 002-TEAMNAME02 " + inn.getBatting_team().getTeamBadge());
// 			
// 			Inning inning2 = match.getMatch().getInning().get(1);
// 		   // boolean homeIsBatting = match.getSetup().getHomeTeamId() == inning2.getBattingTeamId();	
// 	 		
// 	 		
//
// 	 			DoadWriteToTrio(print_writer,"tabfield:set_value_no_update 003-Header AFTER " + CricketFunctions.OverBalls(
// 	 			            inning2.getTotalOvers(),
// 	 			            inning2.getTotalBalls()) + " OVERS");
//
// 	 			DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 101-RUNS01 " + IndexController.MatchStats.getInningCompare().getTotalRuns()
// 	 			        + "-" + IndexController.MatchStats.getInningCompare().getTotalWickets());
//
// 	 			DoadWriteToTrio(print_writer,"tabfield:set_value_no_update 102-RUNS02 "
// 	 			        + inning2.getTotalRuns()+ "-" + inning2.getTotalWickets());
//			}
//			}
// 		
// 		}
	private void sendVizMSEComparisonPage(String pageName,
            MatchAllData match,
            String ipAddress,
            String showName) {

		String[] urls = getShowIdAndURL(pageName,
		match,ipAddress,"Comparison",showName);
		
		String modelUrl = urls[0];
		String putUrl   = urls[1];
		
		String team1Logo = "";
		String team2Logo = "";
		String header = "";
		
		String score1 = "";
		String score2 = "";
		
		Inning inning2 = match.getMatch().getInning().get(1);
		
		for (Inning inn : match.getMatch().getInning()) {
		
		if (CricketUtil.YES.equalsIgnoreCase(inn.getIsCurrentInning())) {
		
		team1Logo = inn.getBowling_team().getTeamBadge();
		team2Logo = inn.getBatting_team().getTeamBadge();
		
		header = "AFTER "
		+ CricketFunctions.OverBalls(
		  inning2.getTotalOvers(),
		  inning2.getTotalBalls())
		+ " OVERS";
		
		score1 = IndexController.MatchStats
		.getInningCompare()
		.getTotalRuns()
		+ "-"
		+ IndexController.MatchStats
		.getInningCompare()
		.getTotalWickets();
		
		score2 = inning2.getTotalRuns()
		+ "-"
		+ inning2.getTotalWickets();
		
		break;
		}
		}
		
		String payloadXml =
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
		+ "<payload xmlns=\"http://www.vizrt.com/types\"\n"
		+ "         model=\"" + modelUrl + "\">\n"
		
		+ "  <field name=\"001-TEAMNAME\">"
		+ "<value>" + team1Logo + "</value></field>\n"
		
		+ "  <field name=\"002-TEAMNAME02\">"
		+ "<value>" + team2Logo + "</value></field>\n"
		
		+ "  <field name=\"003-Header\">"
		+ "<value>" + header + "</value></field>\n"
		
		+ "  <field name=\"101-RUNS01\">"
		+ "<value>" + score1 + "</value></field>\n"
		
		+ "  <field name=\"102-RUNS02\">"
		+ "<value>" + score2 + "</value></field>\n"
		
		+ "</payload>";
		
		putMethod(putUrl, payloadXml);
		}
 	

 	
// 	private void populateTarget(PrintWriter print_writer, MatchAllData match,String cat) {
// 	    
// 		
// 	//	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " + cat.toUpperCase());
// 		
//// 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " +
////			    (cat.equalsIgnoreCase("MEN") ? "MENS" : "WOMENS"));
// 	    StringBuilder sb = new StringBuilder();
// 	    System.out.println("match.getMatch().getInning().get(1).getBatting_team().getTeamBadge()" +match.getMatch().getInning().get(1).getBatting_team().getTeamBadge());
// 	    sb.append("tabfield:set_value_no_update 001-TARGET ").append(CricketFunctions.GetTargetData(match).getTargetRuns()).append(return_key).append(line_feed);
// 	    sb.append("tabfield:set_value_no_update 001-TEAMNAME ").append(match.getMatch().getInning().get(1).getBatting_team().getTeamBadge()).append(return_key).append(line_feed);
// 	    sb.append("tabfield:set_value_no_update 003-Header TARGET").append(return_key).append(line_feed);
//
// 	    print_writer.print(sb.toString());
// 	    print_writer.flush();
// 	}
		 	private void sendVizMSETargetPage(String pageName,
		            MatchAllData match,
		            String ipAddress,
		            String showName) {
		
		String[] urls = getShowIdAndURL(
		pageName,
		match,
		ipAddress,
		"Target",
		showName);
		
		String modelUrl = urls[0];
		String putUrl   = urls[1];
		
		String targetRuns = String.valueOf(
		CricketFunctions.GetTargetData(match).getTargetRuns());
		
		String teamBadge = match.getMatch()
		      .getInning()
		      .get(1)
		      .getBatting_team()
		      .getTeamBadge();
		
		String payloadXml =
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
		+ "<payload xmlns=\"http://www.vizrt.com/types\"\n"
		+ "         model=\"" + modelUrl + "\">\n"
		
		+ "  <field name=\"001-TARGET\">"
		+ "<value>" + targetRuns + "</value></field>\n"
		
		+ "  <field name=\"001-TEAMNAME\">"
		+ "<value>" + teamBadge + "</value></field>\n"
		
		+ "  <field name=\"003-Header\">"
		+ "<value>TARGET</value></field>\n"
		
		+ "</payload>";
		
		putMethod(putUrl, payloadXml);
		}
// 	private void populateLASTXBALLS(PrintWriter print_writer,MatchAllData match,int ballno) {
// 		
// 		this_data_str = new ArrayList<String>();
//		this_data_str.add(CricketFunctions.getlastthirtyballsdata(match, "-", match.getEventFile().getEvents(), ballno));	
//		
//		
//		for(Inning inn : match.getMatch().getInning()) {
//			if(inn.getIsCurrentInning().equalsIgnoreCase("YES")) {
//			//	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-TeamRefName "+ inn.getBatting_team().getTeamBadge());
//			}
//		}
//		
//		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  003-Header " + "LAST " + ballno + " BALLS");
//		
//		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 100-HEADER "+  "RUN"  + 
//				CricketFunctions.Plural(Integer.valueOf(this_data_str.get(this_data_str.size()-1).split("-")[0])).toUpperCase());
//		
//		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 101-RUNS "+  Integer.valueOf(this_data_str.get(this_data_str.size()-1).split("-")[0]));
//		
//		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 102-HEADER "+  "WICKET" + 
//				CricketFunctions.Plural(Integer.valueOf(this_data_str.get(this_data_str.size()-1).split("-")[1])).toUpperCase());
//		
//		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 103-BALLS "+  Integer.valueOf(this_data_str.get(this_data_str.size()-1).split("-")[1]));
// 	
// 		
// 	}
// 	private void populateProjected(PrintWriter print_writer,MatchAllData match,String cat) {
// 		
// 		// DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " + cat.toUpperCase());
// 		 
//// 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " +
////			    (cat.equalsIgnoreCase("MEN") ? "MENS" : "WOMENS"));
// 		
// 		String[] proj_score_rate = new String[CricketFunctions.projectedScore(match).size()];
//	    for (int i = 0; i < CricketFunctions.projectedScore(match).size(); i++) {
//	    	proj_score_rate[i] = CricketFunctions.projectedScore(match).get(i);
//	    	//System.out.println(proj_score_rate[i]);
//        }	
// 		for(Inning inn : match.getMatch().getInning()) {
//			
//			
//			if(inn.getInningNumber() == 1 & inn.getIsCurrentInning().equalsIgnoreCase("YES")) {
//				
//				
//				//team name 
//		// 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-TEAMNAME "+  inn.getBatting_team().getTeamBadge());
//		 		
//		 		
//		 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 106-CURR "+ " @CRR (" +  proj_score_rate[0] + ")");
//		 		
//		 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 105-RUNS03 "+ proj_score_rate[1]);
//		 		
//		 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 102-RR01 "+ " @ " + proj_score_rate[2] + " RPO");
//		 		
//		 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 101-RUNS01 "+ proj_score_rate[3]);
//		 		
//		 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 104-RR02 "+ " @ " + proj_score_rate[4] + " RPO");
//		 		
//		 		
//		 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 103-Runs02 "+ proj_score_rate[5]);
//		 		
//		 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 003-Header "+  "PROJECTED SCORES");
//		 		
//			}
// 		}
// 		
// 	}
		 	private void sendVizMSEProjectedPage(String pageName,
                    MatchAllData match,
                    String ipAddress,
                    String showName) {

				String[] urls = getShowIdAndURL(
				pageName,
				match,
				ipAddress,
				"Projected",
				showName);
				
				String modelUrl = urls[0];
				String putUrl   = urls[1];
				
				// Get projected score data once
				List<String> projScoreRate = CricketFunctions.projectedScore(match);
				
				if (projScoreRate == null || projScoreRate.size() < 6) {
				System.out.println("Projected score data is missing. Size = "
				+ (projScoreRate == null ? 0 : projScoreRate.size()));
				return;
				}
				
				boolean currentFirstInnings = false;
				
				for (Inning inn : match.getMatch().getInning()) {
				
				if (inn.getInningNumber() == 1
				&& CricketUtil.YES.equalsIgnoreCase(inn.getIsCurrentInning())) {
				
				currentFirstInnings = true;
				break;
				}
				}
				
				// Same behavior as Trio logic
				if (!currentFirstInnings) {
				return;
				}
				
				String payloadXml =
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<payload xmlns=\"http://www.vizrt.com/types\"\n"
				+ "         model=\"" + modelUrl + "\">\n"
				
				+ "  <field name=\"003-Header\">"
				+ "<value>PROJECTED SCORES</value></field>\n"
				
				+ "  <field name=\"106-CURR\">"
				+ "<value>@CRR (" + projScoreRate.get(0) + ")</value></field>\n"
				
				+ "  <field name=\"105-RUNS03\">"
				+ "<value>" + projScoreRate.get(1) + "</value></field>\n"
				
				+ "  <field name=\"102-RR01\">"
				+ "<value>@ " + projScoreRate.get(2) + " RPO</value></field>\n"
				
				+ "  <field name=\"101-RUNS01\">"
				+ "<value>" + projScoreRate.get(3) + "</value></field>\n"
				
				+ "  <field name=\"104-RR02\">"
				+ "<value>@ " + projScoreRate.get(4) + " RPO</value></field>\n"
				
				+ "  <field name=\"103-Runs02\">"
				+ "<value>" + projScoreRate.get(5) + "</value></field>\n"
				
				+ "</payload>";
				
				putMethod(putUrl, payloadXml);
				}
		 	
		 	private void sendVizMSETossPage(String pageName,
                    MatchAllData match,
                    int teamId,
                    String val,
                    String ipAddress,
                    String showName) {

String[] urls = getShowIdAndURL(
pageName,
match,
ipAddress,
"TOSS",
showName);

String modelUrl = urls[0];
String putUrl = urls[1];

String data = "";
String teamName = "";

if (match.getSetup().getHomeTeamId() == teamId) {

data = match.getSetup().getHomeTeam().getTeamName1()
    + " WON THE TOSS AND "
    + val;

teamName = match.getSetup().getHomeTeam().getTeamBadge();

} else {

data = match.getSetup().getAwayTeam().getTeamName1()
    + " WON THE TOSS AND "
    + val;

teamName = match.getSetup().getAwayTeam().getTeamBadge();
}

String payloadXml =
"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
+ "<payload xmlns=\"http://www.vizrt.com/types\"\n"
+ "         model=\"" + modelUrl + "\">\n"

+ "  <field name=\"100-LINE\">"
+ "<value>WON THE TOSS</value></field>\n"

+ "  <field name=\"101-LINE\">"
+ "<value>" + val + "</value></field>\n"

+ "</payload>";

putMethod(putUrl, payloadXml);
}
// 	private void populateToss(PrintWriter print_writer,MatchAllData match,String cat,int teamid,String val) {
// 		
// 		
// 	//	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-EVENT-NAME "+  cat.toUpperCase());
// 		
// 		
//// 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-EVENT-NAME " +
////			    (cat.equalsIgnoreCase("MEN") ? "MENS" : "WOMENS"));
//// 		if(teamid == match.getSetup().getHomeTeamId()) {
//// 			//hometeam
//// 	 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 002-TEAM_REF-NAME "+  match.getSetup().getHomeTeam().getTeamBadge());
//// 		}else {
//// 			//awayteam
//// 	 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 002-TEAM_REF-NAME "+  match.getSetup().getAwayTeam().getTeamBadge());
//// 		}
// 		
// 		String data = "",teamName = "";
//		if(match.getSetup().getHomeTeamId()== Integer.valueOf(teamid)) {
//			data = match.getSetup().getHomeTeam().getTeamName1() + " WON THE TOSS AND" + " "+  val;
//			teamName = match.getSetup().getHomeTeam().getTeamBadge();
//		}else {
//			data = match.getSetup().getAwayTeam().getTeamName1() + " WON THE TOSS AND" + " "+ val;
//			teamName = match.getSetup().getAwayTeam().getTeamBadge();
//		}
// 		
// 		
// 		//elected
// 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 100-LINE "+  "WON THE TOSS");
// 		
// 		//result
// 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 101-LINE "+ val);
// 	}
 	
 	private void populateVictory(PrintWriter print_writer,MatchAllData match,String cat) {
 		System.out.println(CricketFunctions.GenerateMatchSummaryStatus(2, match, CricketUtil.FULL, "", "T20_MUMBAI", true).getTargetOrResult());
 		
 		String teamName = CricketFunctions.GenerateMatchSummaryStatus(2, match, CricketUtil.FULL, "", "T20_MUMBAI", 
 				true).getTargetOrResult().split("win")[0];
 		String result = "WIN " + CricketFunctions.GenerateMatchSummaryStatus(2, match, CricketUtil.FULL, "", "T20_MUMBAI", 
 				true).getTargetOrResult().split("win")[1].replace("remaining", "to spare").toUpperCase();
// 		if(match.getSetup().getHomeTeam().getTeamName1().trim().equalsIgnoreCase(teamName.trim())) {
// 			DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-TEAMNAME "+  match.getSetup().getHomeTeam().getTeamBadge());
// 		}else {
// 			DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-TEAMNAME "+  match.getSetup().getAwayTeam().getTeamBadge());
// 		}
 		
 		
 	//	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-EVENT-NAME "+  cat.toUpperCase());
 		
// 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " +
//			    (cat.equalsIgnoreCase("MEN") ? "MENS" : "WOMENS"));
 		
 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 003-Header "+  result);
 		
 		
 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 004-Header "+  "");
 		
// 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-EVENT-NAME "+  cat.toUpperCase());
// 		if(match.getSetup().getTossWinningTeam() == match.getSetup().getHomeTeamId()) {
// 			//hometeam
// 	 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 002-TEAM_REF_NAME "+  match.getSetup().getHomeTeam().getTeamBadge());
// 		}else {
// 			//awayteam
// 	 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 002-TEAM_REF_NAME "+  match.getSetup().getAwayTeam().getTeamBadge());
// 		}
// 		//elected
// 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 100-LINE "+  "WON THE TOSS");
// 		
// 		//result
// 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 101-LINE "+ "CHOSE TO"  + " " + match.getSetup().getTossWinningDecision());
 	}
 	
 	private void populatePARTNERSHIP(PrintWriter print_writer,MatchAllData match,CricketService cricketService) {
 		
 		String Left_Batsman ="",Right_Batsman="";
 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  300-HEADER "+  "CURRENT PARTNERSHIP");
 		
 		for(Inning inn : match.getMatch().getInning()) {
			if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
				DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  000-TeamRefName "+  inn.getBatting_team().getTeamBadge());
				
				DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 503-SELECT-IMPACT 0");
				
				Left_Batsman = inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstPlayer().getTicker_name();
				Right_Batsman = inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondPlayer().getTicker_name();
				
				//name
				DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  402-NAME "+  Left_Batsman);
				DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  502-NAME "+  Right_Batsman);
				
				//photo
				
				DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 401-IMAGE "+ "C:\\\\Images\\\\ISPL\\\\Action_Images\\"
		       			+ inn.getBatting_team().getTeamBadge().toUpperCase().substring(0, 3) + "_" +inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION);
				DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 501-IMAGE "+ "C:\\\\Images\\\\ISPL\\\\Action_Images\\"
		       			+ inn.getBatting_team().getTeamBadge().toUpperCase().substring(0, 3) + "_" +inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION);
				
				//runs
				DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  301-RUNS "+  inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalRuns()+ "*");
				
				//ball
				DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  302-BALLS "+ "OFF "+ inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalBalls());
				
				//impact
				if(!CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn.getInningNumber(), inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstPlayer().getPlayerId()).isEmpty()) {
					switch(CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn.getInningNumber() ,inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstPlayer().getPlayerId())) {
					case "IMP_IN":
						DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 503-SELECT-IMPACT 1");
						break;
					}
				}
				if(!CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn.getInningNumber(), inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondPlayer().getPlayerId()).isEmpty()) {
					switch(CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn.getInningNumber() ,inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondPlayer().getPlayerId())) {
					case "IMP_IN":
						DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 504-SELECT-IMPACT 1");
						break;
					}
				}	
			}
		}
 		
 	}
 	
// 	private void populateRUNRATE(PrintWriter print_writer,MatchAllData match,CricketService cricketService,String cat) {
// 		
// 		
// 		// DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " + cat.toUpperCase());
// 		 
//// 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " +
////			    (cat.equalsIgnoreCase("MEN") ? "MENS" : "WOMENS"));
// 		for(Inning inn : match.getMatch().getInning()) {
// 			if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
// 				// DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-TEAMNAME "+  inn.getBatting_team().getTeamBadge());
// 				 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 101-RUNS "+ inn.getRunRate());
// 				 
// 				 
// 				 
// 				 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 103-BALLS "+  CricketFunctions.generateRunRate(CricketFunctions.GetTargetData(match).getRemaningRuns(), 
// 						0,CricketFunctions.GetTargetData(match).getRemaningBall(), 2,match));
// 				 
// 				 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 003-Header "+  "RUN RATES");
// 				 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 100-HEADER "+  "CURRENT" );
// 				 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 102-HEADER "+  "REQUIRED" );
// 				
// 			}
// 		}
// 	}
 	
		 	private void sendVizMSERunRatePage(String pageName,
		            MatchAllData match,
		            CricketService cricketService,
		            String ipAddress,
		            String showName) {
		
		String[] urls = getShowIdAndURL(
		pageName,
		match,
		ipAddress,
		"RunsAndBall",
		showName);
		
		String modelUrl = urls[0];
		String putUrl = urls[1];
		
		String currentRunRate = "";
		String requiredRunRate = "";
		
		for (Inning inn : match.getMatch().getInning()) {
		
		if (CricketUtil.YES.equalsIgnoreCase(inn.getIsCurrentInning())) {
		
		currentRunRate = String.valueOf(inn.getRunRate());
		
		requiredRunRate = CricketFunctions.generateRunRate(
		CricketFunctions.GetTargetData(match).getRemaningRuns(),
		0,
		CricketFunctions.GetTargetData(match).getRemaningBall(),
		2,
		match);
		
		break;
		}
		}
		
		String payloadXml =
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
		+ "<payload xmlns=\"http://www.vizrt.com/types\"\n"
		+ "         model=\"" + modelUrl + "\">\n"
		
		+ "  <field name=\"003-Header\"><value>RUN RATES</value></field>\n"
		+ "  <field name=\"100-HEADER\"><value>CURRENT</value></field>\n"
		+ "  <field name=\"101-RUNS\"><value>" + currentRunRate + "</value></field>\n"
		+ "  <field name=\"102-HEADER\"><value>REQUIRED</value></field>\n"
		+ "  <field name=\"103-BALLS\"><value>" + requiredRunRate + "</value></field>\n"
		
		+ "</payload>";
		
		putMethod(putUrl, payloadXml);
		}
 	
// 	private void populateLastxovers(PrintWriter print_writer,MatchAllData match,int overnumber,String cat) {
// 		System.out.println("coming insde");
// 		
// 		int innnuber =0;
//		for (Inning inn : match.getMatch().getInning()) {
//		if (inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
//			 innnuber = inn.getInningNumber();	
//		}
//	} 
// 		// DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " + cat.toUpperCase());
// 		 List<OverByOverData> manhattan = CricketFunctions.getOverByOverData(match, innnuber, "MANHATTAN", match.getEventFile().getEvents());
// 	    
// 		 
// 		 int Runs =0 ,wickets= 0; 
// 		 System.out.println(manhattan.size()-1);
// 		 for(int i=1; i<manhattan.size();i++) {
// 			 if(i >= overnumber) {
// 				Runs += manhattan.get(i).getOverTotalRuns();
// 		        wickets += manhattan.get(i).getOverTotalWickets();
// 			 }
// 		} 
//// 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " +
////			    (cat.equalsIgnoreCase("MEN") ? "MENS" : "WOMENS"));
// 		for(Inning inn : match.getMatch().getInning()) {
// 			if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
// 		//		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-TEAMNAME "+  inn.getBatting_team().getTeamBadge());
// 				 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 101-RUNS "+ Runs);
// 				 
// 				 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 103-BALLS "+  wickets);
// 				
// 				 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 102-HEADER "+  "WICKET" + CricketFunctions.Plural(wickets).toUpperCase());
// 				 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 100-HEADER "+  "RUNS" );
// 				 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 003-Header "+  "LAST " + 
// 						((inn.getTotalOvers() + 1)- overnumber) + "." + inn.getTotalBalls() + " OVERS" );
// 				
// 			}
// 		}
// 	}
		 	private void sendVizMSELastXOversPage(String pageName,
		            MatchAllData match,
		            int overNumber,
		            String ipAddress,
		            String showName) {
		
		String[] urls = getShowIdAndURL(
				pageName,
		match,
		ipAddress,
		"RunsAndBall",
		showName);
		
		String modelUrl = urls[0];
		String putUrl = urls[1];
		
		int inningNumber = 0;
		
		for (Inning inn : match.getMatch().getInning()) {
		if (CricketUtil.YES.equalsIgnoreCase(inn.getIsCurrentInning())) {
		inningNumber = inn.getInningNumber();
		break;
		}
		}
		
		List<OverByOverData> manhattan =
		CricketFunctions.getOverByOverData(
		match,
		inningNumber,
		"MANHATTAN",
		match.getEventFile().getEvents());
		
		int runs = 0;
		int wickets = 0;
		
		for (int i = 1; i < manhattan.size(); i++) {
		
		if (i >= overNumber) {
		
		runs += manhattan.get(i).getOverTotalRuns();
		wickets += manhattan.get(i).getOverTotalWickets();
		}
		}
		
		String header = "";
		String wicketHeader = "";
		
		for (Inning inn : match.getMatch().getInning()) {
		
		if (CricketUtil.YES.equalsIgnoreCase(inn.getIsCurrentInning())) {
		
		header = "LAST "
		+ ((inn.getTotalOvers() + 1) - overNumber)
		+ "."
		+ inn.getTotalBalls()
		+ " OVERS";
		
		wicketHeader =
		"WICKET"
		+ CricketFunctions.Plural(wickets).toUpperCase();
		
		break;
		}
		}
		
		String payloadXml =
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
		+ "<payload xmlns=\"http://www.vizrt.com/types\"\n"
		+ "         model=\"" + modelUrl + "\">\n"
		
		+ "  <field name=\"003-Header\"><value>" + header + "</value></field>\n"
		+ "  <field name=\"100-HEADER\"><value>RUNS</value></field>\n"
		+ "  <field name=\"101-RUNS\"><value>" + runs + "</value></field>\n"
		+ "  <field name=\"102-HEADER\"><value>" + wicketHeader + "</value></field>\n"
		+ "  <field name=\"103-BALLS\"><value>" + wickets + "</value></field>\n"
		
		+ "</payload>";
		
		putMethod(putUrl, payloadXml);
		}
 	
 	private void populateMatchID(PrintWriter print_writer,MatchAllData match,CricketService cricketService,String cat) {
 		
// 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " +
//			    (cat.equalsIgnoreCase("MEN") ? "MENS" : "WOMENS"));
 		
 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  004-LOCATION "+  "LIVE FROM WANKHEDE STADIUM");
 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  003-MATCH-NUMBER "+  match.getSetup().getMatchIdent());
 		
 //		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-TEAMNAME "+  match.getSetup().getHomeTeam().getTeamBadge());
 //		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 002-TEAMNAME "+  match.getSetup().getAwayTeam().getTeamBadge());
 		
 		
 	//	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 101-PLAYER_IMAGE "+  match.getSetup().getHomeTeam().getTeamBadge());
 	//	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 102-PLAYER_IMAGE "+  match.getSetup().getAwayTeam().getTeamBadge());

 	}
 	
 	private void populateMOSTRUNS(PrintWriter print_writer,MatchAllData match,CricketService cricketService, List<Tournament> tournament_stats,String plID,String cat) {
 		
 		 int rowId = 0;
 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  003-HEADER MOST RUNS");
  		for(int i = 0; i <= tournament_stats.size() - 1 ; i++) {
			  rowId = rowId + 1;
			if(rowId <= 5) {
				
				
				DoadWriteToTrio(print_writer,  "tabfield:set_value_no_update 004-SELECT_CAP " + 1);
				
				DoadWriteToTrio(print_writer,  "tabfield:set_value_no_update " + rowId + "02-NAME " +  tournament_stats.get(i).getPlayer().getTicker_name());
				
				DoadWriteToTrio(print_writer,  "tabfield:set_value_no_update " + rowId + "03-SELECT-STAT " + 1);
				
				DoadWriteToTrio(print_writer,  "tabfield:set_value_no_update " + rowId + "04-STAT " + rowId);
				DoadWriteToTrio(print_writer,  "tabfield:set_value_no_update " + rowId + "05-STAT " + tournament_stats.get(i).getRuns());
				DoadWriteToTrio(print_writer,  "tabfield:set_value_no_update " + rowId + "06-SELECT_CAP " + 0);
				
//				for(Team tm:cricketService.getTeams()) {
//					if(tm.getTeamId() == tournament_stats.get(i).getPlayer().getTeamId()) {
//						DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 3-TEAM-NAME " + (rowId-1) + " " + tm.getTeamBadge());
//						
//					}
//				}
		
				for(Team tm:cricketService.getTeams()) {
					if(tm.getTeamId() == tournament_stats.get(rowId-1).getPlayer().getTeamId()) {
						DoadWriteToTrio(print_writer,  "tabfield:set_value_no_update " + rowId + "00-TEAMNAME " +  tm.getTeamBadge());
						
						DoadWriteToTrio(print_writer, "tabfield:set_value_no_update " + rowId + "01-PLAYER-IMAGE " + 
			    				" C:\\\\Images\\\\T20_MUMBAI\\\\PHOTOS\\"
			    		      			+ cat +  "\\\\BIG_1024\\\\"   +  tm.getTeamName4()+ "\\\\" +
			    							tournament_stats.get(rowId-1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION);
						
					}
				}	
			}
		}
 	}
 	
 	private void populateTapeBallMostWickets(PrintWriter print_writer,MatchAllData match,CricketService cricketService, List<BestStats> tape_balls,String plID) {
 		
		 int rowId = 0;
		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  000-MATCH-NUMBER MOST WICKETS ISPL SWING BALL OVERS");
		
		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 700-SELECT-STAT-HEAD 2");
		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 801-STAT-HEAD OVERS");
		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 802-STAT-HEAD WKTS");
		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 803-STAT-HEAD ECON");
		for(int i = 0; i <= tape_balls.size() - 1 ; i++) {
			  rowId = rowId + 1;
			if(rowId <= 5) {
				
				DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 4-SELECT_VALUE " + (rowId-1) + " " + 2);
				DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 1-SELECT_TEAM_NAME " + (rowId-1) + " " + 1);
				
				DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 5-STAT-VALUE " + (rowId-1) + " " + CricketFunctions.OverBalls(0, tape_balls.get(i).getBalls()));
				DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 6-STAT-VALUE " + (rowId-1) + " " + tape_balls.get(i).getWickets());
				DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 7-STAT-VALUE " + (rowId-1) + " " + CricketFunctions.getEconomy(tape_balls.get(i).getRuns(), 
						tape_balls.get(i).getBalls(), 2, "-"));
				
				
				DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 2-PLAYER-NAME " + (rowId-1) + " " + tape_balls.get(i).getPlayer().getFull_name());
				
				for(Team tm:cricketService.getTeams()) {
					if(tm.getTeamId() == tape_balls.get(i).getPlayer().getTeamId()) {
						DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 3-TEAM-NAME " + (rowId-1) + " " + tm.getTeamName1());
						
					}
				}
				
				if(Integer.valueOf(plID.split("_")[0]) == rowId) {
					DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 0-SELECT_HIGHLIGHT " + (rowId-1) + " 1");
					for(Team tm:cricketService.getTeams()) {
						if(tm.getTeamId() == tape_balls.get(rowId-1).getPlayer().getTeamId()) {
							DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-TeamRefName "+  tm.getTeamBadge());
							DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 501-IMAGE "+ "C:\\\\Images\\\\ISPL\\\\Action_Images\\"
					      			 + tm.getTeamBadge().toUpperCase().substring(0, 3) + "_"+ tape_balls.get(rowId-1).getPlayer().getPhoto() 
					      			 + CricketUtil.PNG_EXTENSION);
							
						}
					}	
				}else {
					DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 0-SELECT_HIGHLIGHT " + (rowId-1) + " 0");
				}
			}
		}
	}
 	
 	private void populateMVPLEADERBOARD(PrintWriter print_writer,MatchAllData match,CricketService cricketService) {
		
		
 	  	List<mvp_leaderBoard.Player> finalFivePlayers = new ArrayList<>();
 	  	
 	  	//topdatalist
 	  	for (int i = 0; i < mvp.getData().getTop().size(); i++) {
 	  	    finalFivePlayers.add(mvp.getData().getTop().get(i));
 	  	}
 	  	for (int i = 0; i < 2 && i < mvp.getData().getList().size(); i++) {
 	  	    finalFivePlayers.add(mvp.getData().getList().get(i));
 	  	}
 	  	
 	  	 int rowId = 0;
 			DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  000-MATCH-NUMBER MOST VALUABLE PLAYER");
 			
 			
 			 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 700-SELECT-STAT-HEAD 0");
 			 DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 0-SELECT_HIGHLIGHT 0 1");
 			 DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 0-SELECT_HIGHLIGHT 1 0");
 			 DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 0-SELECT_HIGHLIGHT 2 0");
 			 DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 0-SELECT_HIGHLIGHT 3 0");
 			 DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 0-SELECT_HIGHLIGHT 4 0");
 			 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 801-STAT-HEAD FINAL POINTS");
 			 
 	  	for (int j = 0; j < finalFivePlayers.size(); j++) {
 	  		String player_id = finalFivePlayers.get(j).getPlayerId();
 	  		rowId = rowId + 1;
 	  		
 	  		if(rowId <= 5) {
 	  			DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 1-SELECT_TEAM_NAME " + (rowId-1) + " " + 1);
 	  			
 	  			
 	  			DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 5-STAT-VALUE " + (rowId-1) + " " + finalFivePlayers.get(j).getFinalPoints());
 	  			
 	  			player = cricketService.getAllPlayer().stream().filter(plyr -> Long.valueOf(player_id).equals(plyr.getOnlineId())).findAny().orElse(null);
 	  	  		
 	  	  		team = cricketService.getTeams().stream().filter(tm -> tm.getTeamId() == player.getTeamId()).findAny().orElse(null);
 	  	  		

 	  			DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 2-PLAYER-NAME " + (rowId-1) + " " + player.getFull_name());
 	  			
 	  			DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 3-TEAM-NAME " + (rowId-1) + " " + team.getTeamName1());
 	  			
 	  			if(rowId == 1) {
	  				DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-TeamRefName "+  team.getTeamBadge());
	  				
	  				DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 501-IMAGE "+ "C:\\\\Images\\\\ISPL\\\\Action_Images\\"
 			      			 + team.getTeamBadge().toUpperCase().substring(0, 3) + "_"+ player.getPhoto() + CricketUtil.PNG_EXTENSION);	
	  			}
 	  		}
 	  	}
 	}
 	
 	
 	private void populateMOSTNINES(PrintWriter print_writer,MatchAllData match,CricketService cricketService, List<Tournament> tournament_stats,String plID) {
 		
		 int rowId = 0;
		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  000-MATCH-NUMBER MOST 9 STREET RUNS");
		
		
		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 700-SELECT-STAT-HEAD 1");
		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 801-STAT-HEAD MTS");
		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 802-STAT-HEAD NINES");
		for(int i = 0; i <= tournament_stats.size() - 1 ; i++) {
			  rowId = rowId + 1;
			if(rowId <= 5) {
				
				DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 4-SELECT_VALUE " + (rowId-1) + " " + 1);
				DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 1-SELECT_TEAM_NAME " + (rowId-1) + " " + 1);
				
				DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 5-STAT-VALUE " + (rowId-1) + " " + tournament_stats.get(i).getMatches());
				DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 6-STAT-VALUE " + (rowId-1) + " " + tournament_stats.get(i).getNines());
				
				
				DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 2-PLAYER-NAME " + (rowId-1) + " " + tournament_stats.get(i).getPlayer().getFull_name());
				
				
				
				for(Team tm:cricketService.getTeams()) {
					if(tm.getTeamId() == tournament_stats.get(i).getPlayer().getTeamId()) {
						DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 3-TEAM-NAME " + (rowId-1) + " " + tm.getTeamName1());
						
					}
				}
				
				if(Integer.valueOf(plID.split("_")[0]) == rowId) {
					DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 0-SELECT_HIGHLIGHT " + (rowId-1) + " 1");
					for(Team tm:cricketService.getTeams()) {
						if(tm.getTeamId() == tournament_stats.get(rowId-1).getPlayer().getTeamId()) {
							DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-TeamRefName "+  tm.getTeamBadge());
							DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 501-IMAGE "+ "C:\\\\Images\\\\ISPL\\\\Action_Images\\"
					      			 + tm.getTeamBadge().toUpperCase().substring(0, 3) + "_"+ tournament_stats.get(rowId-1).getPlayer().getPhoto() 
					      			 + CricketUtil.PNG_EXTENSION);
							
						}
					}	
				}else {
					DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 0-SELECT_HIGHLIGHT " + (rowId-1) + " 0");
				}
			}
		}
	}
 	
 	private void populateMOSTSIXES(PrintWriter print_writer,MatchAllData match,CricketService cricketService, List<Tournament> tournament_stats,String plID) {
 		
		 int rowId = 0;
		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  000-MATCH-NUMBER MOST SIXES");
		
		
		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 700-SELECT-STAT-HEAD 1");
		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 801-STAT-HEAD MTS");
		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 802-STAT-HEAD SIXES");
		for(int i = 0; i <= tournament_stats.size() - 1 ; i++) {
			  rowId = rowId + 1;
			if(rowId <= 5) {
				
				DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 4-SELECT_VALUE " + (rowId-1) + " " + 1);
				DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 1-SELECT_TEAM_NAME " + (rowId-1) + " " + 1);
				
				DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 5-STAT-VALUE " + (rowId-1) + " " + tournament_stats.get(i).getMatches());
				DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 6-STAT-VALUE " + (rowId-1) + " " + tournament_stats.get(i).getSixes());
				
				
				DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 2-PLAYER-NAME " + (rowId-1) + " " + tournament_stats.get(i).getPlayer().getFull_name());
				
				
				
				for(Team tm:cricketService.getTeams()) {
					if(tm.getTeamId() == tournament_stats.get(i).getPlayer().getTeamId()) {
						DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 3-TEAM-NAME " + (rowId-1) + " " + tm.getTeamName1());
						
					}
				}
				
				if(Integer.valueOf(plID.split("_")[0]) == rowId) {
					DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 0-SELECT_HIGHLIGHT " + (rowId-1) + " 1");
					for(Team tm:cricketService.getTeams()) {
						if(tm.getTeamId() == tournament_stats.get(rowId-1).getPlayer().getTeamId()) {
							DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-TeamRefName "+  tm.getTeamBadge());
							DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 501-IMAGE "+ "C:\\\\Images\\\\ISPL\\\\Action_Images\\"
					      			 + tm.getTeamBadge().toUpperCase().substring(0, 3) + "_"+ tournament_stats.get(rowId-1).getPlayer().getPhoto() 
					      			 + CricketUtil.PNG_EXTENSION);
							
						}
					}	
				}else {
					DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 0-SELECT_HIGHLIGHT " + (rowId-1) + " 0");
				}
			}
		}
	}
 	private void populateMOSTFOURS(PrintWriter print_writer,MatchAllData match,CricketService cricketService, List<Tournament> tournament_stats,String plID) {
 		
		 int rowId = 0;
		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  000-MATCH-NUMBER MOST FOURS");
		
		
		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 700-SELECT-STAT-HEAD 1");
		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 801-STAT-HEAD MTS");
		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 802-STAT-HEAD FOURS");
		for(int i = 0; i <= tournament_stats.size() - 1 ; i++) {
			  rowId = rowId + 1;
			if(rowId <= 5) {
				
				DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 4-SELECT_VALUE " + (rowId-1) + " " + 1);
				DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 1-SELECT_TEAM_NAME " + (rowId-1) + " " + 1);
				
				DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 5-STAT-VALUE " + (rowId-1) + " " + tournament_stats.get(i).getMatches());
				DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 6-STAT-VALUE " + (rowId-1) + " " + tournament_stats.get(i).getFours());
				
				
				DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 2-PLAYER-NAME " + (rowId-1) + " " + tournament_stats.get(i).getPlayer().getFull_name());
				
				
				
				for(Team tm:cricketService.getTeams()) {
					if(tm.getTeamId() == tournament_stats.get(i).getPlayer().getTeamId()) {
						DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 3-TEAM-NAME " + (rowId-1) + " " + tm.getTeamName1());
						
					}
				}
				
				if(Integer.valueOf(plID.split("_")[0]) == rowId) {
					DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 0-SELECT_HIGHLIGHT " + (rowId-1) + " 1");
					for(Team tm:cricketService.getTeams()) {
						if(tm.getTeamId() == tournament_stats.get(rowId-1).getPlayer().getTeamId()) {
							DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-TeamRefName "+  tm.getTeamBadge());
							DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 501-IMAGE "+ "C:\\\\Images\\\\ISPL\\\\Action_Images\\"
					      			 + tm.getTeamBadge().toUpperCase().substring(0, 3) + "_"+ tournament_stats.get(rowId-1).getPlayer().getPhoto() 
					      			 + CricketUtil.PNG_EXTENSION);
							
						}
					}	
				}else {
					DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 0-SELECT_HIGHLIGHT " + (rowId-1) + " 0");
				}
			}
		}
	}
 	
 	private void populateMOSTWKTS(PrintWriter print_writer,MatchAllData match,CricketService cricketService, List<Tournament> tournament_stats,String plID,String cat) {
 		
		 int rowId = 0;
		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  003-HEADER MOST WICKETS");
		
		for(int i = 0; i <= tournament_stats.size() - 1 ; i++) {
			  rowId = rowId + 1;
			if(rowId <= 5) {
				
				DoadWriteToTrio(print_writer,  "tabfield:set_value_no_update 004-SELECT_CAP " + 2);
				
				DoadWriteToTrio(print_writer,  "tabfield:set_value_no_update " + rowId + "02-NAME " +  tournament_stats.get(i).getPlayer().getTicker_name());
				
				DoadWriteToTrio(print_writer,  "tabfield:set_value_no_update " + rowId + "03-SELECT-STAT " + 1);
				
				DoadWriteToTrio(print_writer,  "tabfield:set_value_no_update " + rowId + "04-STAT " + rowId);
				DoadWriteToTrio(print_writer,  "tabfield:set_value_no_update " + rowId + "05-STAT " + tournament_stats.get(i).getWickets());
				DoadWriteToTrio(print_writer,  "tabfield:set_value_no_update " + rowId + "06-SELECT_CAP " + 0);
				
//				for(Team tm:cricketService.getTeams()) {
//					if(tm.getTeamId() == tournament_stats.get(i).getPlayer().getTeamId()) {
//						DoadWriteToTrio(print_writer, "table:set_cell_value 900-DATA 3-TEAM-NAME " + (rowId-1) + " " + tm.getTeamBadge());
//					}
//				}
				
				for(Team tm:cricketService.getTeams()) {
					if(tm.getTeamId() == tournament_stats.get(rowId-1).getPlayer().getTeamId()) {
						DoadWriteToTrio(print_writer,  "tabfield:set_value_no_update " + rowId + "00-TEAMNAME " +  tm.getTeamBadge());
						
						System.out.println("CAT" + cat);
						DoadWriteToTrio(print_writer, "tabfield:set_value_no_update " + rowId + "01-PLAYER-IMAGE " + 
			    				" C:\\\\Images\\\\T20_MUMBAI\\\\PHOTOS\\"
			    		      			+ cat +  "\\\\BIG_1024\\\\"   +  tm.getTeamName4()+ "\\\\" +
			    							tournament_stats.get(rowId-1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION);
						
					}
				}	
			}
		}
		
	}
    private void populateEquation(PrintWriter print_writer,MatchAllData match,HeadToHead headToHeads,String cat) {
 		
 		
   // 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " + cat.toUpperCase());
    	
//    	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " +
//			    (cat.equalsIgnoreCase("MEN") ? "MENS" : "WOMENS"));
    	
 	// 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-TEAMNAME "+  match.getMatch().getInning().get(1).getBatting_team().getTeamBadge());

 		//run
 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 101-RUNS "+ CricketFunctions.GetTargetData(match).getRemaningRuns());
 		
 	//	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 006-DATA02 BALL"+ CricketFunctions.Plural(CricketFunctions.GetTargetData(match).getRemaningBall()).toUpperCase());
 		
 		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 003-Header "+  "TARGET");
			 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 100-HEADER "+  "NEED" );
			 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 102-HEADER "+  "FROM" );
 		//balls
 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 103-BALLS "+  CricketFunctions.GetTargetData(match).getRemaningBall());
 //		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 003-DATA02 RUN"+ CricketFunctions.Plural(CricketFunctions.GetTargetData(match).getRemaningRuns()).toUpperCase());
 	}
    
//    private void populateOpenerProfile(PrintWriter print_writer,MatchAllData match,int inn) throws InterruptedException {
// 		
//	 
//	 
//	 if(debut1.equalsIgnoreCase("YES") && debut2.equalsIgnoreCase("YES")) {
//		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 500-SELECT-DATA-TYPE 0");
//		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-HEAD ");
//		 TimeUnit.MILLISECONDS.sleep(500);
//	 }else if(debut1.equalsIgnoreCase("YES") && debut2.equalsIgnoreCase("NO")) {
//		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 500-SELECT-DATA-TYPE 1");
//		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-HEAD "+ profile );
//		 if(profile.equalsIgnoreCase("ISPL SEASON 3")) {
//	 	 		
//	 	 	 	//2
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 0 " +  (tournament2.getMatches() != 0?String.valueOf(tournament2.getMatches()):"-"));
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 1 " +  (tournament2.getRuns() != 0?String.valueOf(tournament2.getRuns()):"-"));
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 2 " +  CricketFunctions.generateStrikeRate(tournament2.getRuns(), tournament2.getBallsFaced(), 0));
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 3 " +  best2);
//	 	 	 	TimeUnit.MILLISECONDS.sleep(500);
//	 	 	}else {
//	 	 		
//	 	 	 	//2
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 0 " +  stat2.getMatches());
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 1 " +  runs2);
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 2 " +  strikeRate2);
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 3 " +  best2);
//	 	 	 	TimeUnit.MILLISECONDS.sleep(500);
//	 	 	}
//	 }else if(debut1.equalsIgnoreCase("NO") && debut2.equalsIgnoreCase("YES")) {
//		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 500-SELECT-DATA-TYPE 2");
//		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-HEAD "+ profile );
//		 TimeUnit.MILLISECONDS.sleep(500);
//		 
//		 if(profile.equalsIgnoreCase("ISPL SEASON 3")) {
//	 	 		//System.out.println("Coming inside");
//	 	 		DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 0 " +  (tournament.getMatches() != 0?String.valueOf(tournament.getMatches()):"-"));
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 1 " +  (tournament.getRuns() != 0?String.valueOf(tournament.getRuns()):"-"));
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 2 " +  CricketFunctions.generateStrikeRate(tournament.getRuns(), tournament.getBallsFaced(), 0));
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 3 " +  best);
//	 	 	 	TimeUnit.MILLISECONDS.sleep(500);
//	 	 	 	
//	 	 	}else {
//	 	 		DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 0 " +  stat.getMatches());
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 1 " +  runs);
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 2 " +  strikeRate);
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 3 " +  best);
//	 	 	 	TimeUnit.MILLISECONDS.sleep(500);
//	 	 	 	
//	 	 	}
//	 }else {
//		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 500-SELECT-DATA-TYPE 3");
//		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-HEAD "+ profile );
//		 if(profile.equalsIgnoreCase("ISPL SEASON 3")) {
//	 	 		//System.out.println("Coming inside");
//	 	 		DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 0 " +  (tournament.getMatches() != 0?String.valueOf(tournament.getMatches()):"-"));
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 1 " +  (tournament.getRuns() != 0?String.valueOf(tournament.getRuns()):"-"));
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 2 " +  CricketFunctions.generateStrikeRate(tournament.getRuns(), tournament.getBallsFaced(), 0));
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 3 " +  best);
//	 	 	 	
//	 	 	 	//2
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 0 " +  (tournament2.getMatches() != 0?String.valueOf(tournament2.getMatches()):"-"));
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 1 " +  (tournament2.getRuns() != 0?String.valueOf(tournament2.getRuns()):"-"));
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 2 " +  CricketFunctions.generateStrikeRate(tournament2.getRuns(), tournament2.getBallsFaced(), 0));
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 3 " +  best2);
//	 	 	 	TimeUnit.MILLISECONDS.sleep(500);
//	 	 	}else {
//	 	 		DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 0 " +  stat.getMatches());
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 1 " +  runs);
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 2 " +  strikeRate);
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 3 " +  best);
//	 	 	 	
//	 	 	 	//2
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 0 " +  stat2.getMatches());
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 1 " +  runs2);
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 2 " +  strikeRate2);
//	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 3 " +  best2);
//	 	 	 	TimeUnit.MILLISECONDS.sleep(500);
//	 	 	}
//	 }
// 	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 302-NAME "+  player.getTicker_name());
// 	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 301-IMAGE "+ "C:\\\\Images\\\\ISPL\\\\PHOTOS\\"
//    			+ team.getTeamName4()+ "\\\\STRAIGHT_1024\\\\" +player.getPhoto()+ CricketUtil.PNG_EXTENSION);
// 	 	//2
// 	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 402_NAME "+  player2.getTicker_name());
// 	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 401-IMAGE "+ "C:\\\\Images\\\\ISPL\\\\PHOTOS\\"
//    			+ team.getTeamName4()+ "\\\\STRAIGHT_1024\\\\" +player2.getPhoto()+ CricketUtil.PNG_EXTENSION);
// 	 	
// 	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-TeamRefName " + team.getTeamBadge() );
// 		//head
// 	 	
// 	 	TimeUnit.MILLISECONDS.sleep(500);
// 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATHEAD 0" +  " MTS");
// 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATHEAD 1" +  " RUNS");
// 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATHEAD 2" +  " SR");
// 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATHEAD 3" +  " BEST");
// 	 	
// 	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 503-SELECT-IMPACT 0");
// 	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 504-SELECT-IMPACT 0");
// 	 	
//		if(!CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn, player.getPlayerId()).isEmpty()) {
//			switch(CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn ,player.getPlayerId())) {
//			case "IMP_IN":
//				DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 503-SELECT-IMPACT 1");
//				break;
//			}
//		}
//		if(!CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn, player2.getPlayerId()).isEmpty()) {
//			switch(CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn ,player2.getPlayerId())) {
//			case "IMP_IN":
//				DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 504-SELECT-IMPACT 1");
//				break;
//			}
//		}
// 	}
    
    
    private void populateOpenerProfile(PrintWriter print_writer,MatchAllData match,int inn,String cat) throws InterruptedException {
 		
   // 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " + cat.toUpperCase());
    		
//    		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " +
//    			    (cat.equalsIgnoreCase("MEN") ? "MENS" : "WOMENS"));
    	
    	
    	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 101-PlayerName01 "+  player.getTicker_name());
    	 	
    	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 101-layerImage01 "+ 
    				"C:\\\\Images\\\\T20_MUMBAI\\\\PHOTOS\\"
    		      			+ cat +  "\\\\STRAIGHT_1024\\\\"   +  team.getTeamName4()+ "\\\\" +
    		      			player.getPhoto()+ CricketUtil.PNG_EXTENSION);
    	 	
    	 	
    	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 101-PlayerName02 "+  player2.getTicker_name());
    	 	
    	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 102-layerImage02 "+ 
    				"C:\\\\Images\\\\T20_MUMBAI\\\\PHOTOS\\"
    		      			+ cat +  "\\\\STRAIGHT_1024\\\\"   +  team.getTeamName4()+ "\\\\" +
    		      			player2.getPhoto()+ CricketUtil.PNG_EXTENSION);
    	 	
    	 //	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-TEAMNAME " + team.getTeamBadge() );
    		//head
    	 	
   		
    	}
   private void populateDoubleProfile(PrintWriter print_writer,MatchAllData match) {
 		
 		//System.out.println("COMONH INSIDE MAIn");
 	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 302-NAME "+  player.getTicker_name());
 	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 301-IMAGE "+ "C:\\\\Images\\\\ISPL\\\\PHOTOS\\"
    			+ team.getTeamName4()+ "\\\\STRAIGHT_1024\\\\" +player.getPhoto()+ CricketUtil.PNG_EXTENSION);
 	 	//2
 	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 402_NAME "+  player2.getTicker_name());
 	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 401-IMAGE "+ "C:\\\\Images\\\\ISPL\\\\PHOTOS\\"
    			+ team.getTeamName4()+ "\\\\STRAIGHT_1024\\\\" +player2.getPhoto()+ CricketUtil.PNG_EXTENSION);
 	 	
 	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-TeamRefName " + team.getTeamBadge() );
 		//head
 	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-HEAD "+ profile );
 		
 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATHEAD 0" +  " MTS");
 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATHEAD 1" +  " RUNS");
 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATHEAD 2" +  " SR");
 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATHEAD 3" +  " BEST");
 	 	
 	 	if(profile.equalsIgnoreCase("ISPL SEASON 3")) {
 	 		//System.out.println("Coming inside");
 	 		DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 0 " +  (tournament.getMatches() != 0?String.valueOf(tournament.getMatches()):"-"));
 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 1 " +  (tournament.getRuns() != 0?String.valueOf(tournament.getRuns()):"-"));
 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 2 " +  CricketFunctions.generateStrikeRate(tournament.getRuns(), tournament.getBallsFaced(), 0));
 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 3 " +  best);
 	 	 	
 	 	 	//2
 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 0 " +  (tournament2.getMatches() != 0?String.valueOf(tournament2.getMatches()):"-"));
 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 1 " +  (tournament2.getRuns() != 0?String.valueOf(tournament2.getRuns()):"-"));
 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 2 " +  CricketFunctions.generateStrikeRate(tournament2.getRuns(), tournament2.getBallsFaced(), 0));
 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 3 " +  best2);
 	 	}else {
 	 		DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 0 " +  stat.getMatches());
 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 1 " +  runs);
 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 2 " +  strikeRate);
 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE1 3 " +  best);
 	 	 	
 	 	 	//2
 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 0 " +  stat2.getMatches());
 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 1 " +  runs2);
 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 2 " +  strikeRate2);
 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 501-DATA-ALL STATVALUE2 3 " +  best2);
 	 	}
 	 	
 	}
   
   private void populateInAT(PrintWriter print_writer,MatchAllData match,int inn,String cat) throws InterruptedException {
 	  
	   
//	   DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " + cat.toUpperCase());
	   
//		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " +
//			    (cat.equalsIgnoreCase("MEN") ? "MENS" : "WOMENS"));
 	  int rowId = 0;
 	  for(BattingCard bc : inning.getBattingCard()) {
 		  rowId = rowId + 1;
			  if(bc.getPlayerId() == player.getPlayerId()) {
				DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 102-PlayerName01 "+  bc.getPlayer().getTicker_name());
				DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 101-layerImage01 "+ 
				"C:\\\\Images\\\\T20_MUMBAI\\\\PHOTOS\\"
		      			+ cat +  "\\\\STRAIGHT_1024\\\\"   +  inning.getBatting_team().getTeamName4()+ "\\\\" +
						bc.getPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION);
			      DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 101-AT "+  rowId);	
			      
			//      DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-TEAMNAME "+  inning.getBatting_team().getTeamBadge());
			   }
 	  	}
			
 	 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 003-MATCH-NUMBER "+  "");
 	  
 	  
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$InAt_Profile$Profile$HeaderGrp$Info$txt_BattingHand*GEOM*TEXT SET " + player.getBattingStyle() + "\0", print_writers);
//			if(WhichProfile.equalsIgnoreCase("DT20")) {
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$InAt_Profile$Profile$HeaderGrp$Info$txt_Age*GEOM*TEXT SET " + "T20 CAREER" + "\0", print_writers);
//			}else if(WhichProfile.equalsIgnoreCase("IT20")){
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$InAt_Profile$Profile$HeaderGrp$Info$txt_Age*GEOM*TEXT SET " + "T20I CAREER" + "\0", print_writers);
//			}else if(WhichProfile.equalsIgnoreCase("T20 MUMBAI")){
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$InAt_Profile$Profile$HeaderGrp$Info$txt_Age*GEOM*TEXT SET " + "T20 MUMBAI CAREER" + "\0", print_writers);
//			}else if(WhichProfile.equalsIgnoreCase("IPL")){
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$InAt_Profile$Profile$HeaderGrp$Info$txt_Age*GEOM*TEXT SET " + "IPL CAREER" + "\0", print_writers);
//			}else if(WhichProfile.equalsIgnoreCase("IPL 2026")){
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$InAt_Profile$Profile$HeaderGrp$Info$txt_Age*GEOM*TEXT SET " + "IPL 2026" + "\0", print_writers);
//			}else if(WhichProfile.equalsIgnoreCase("MT20 SEASON 3")){
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$InAt_Profile$Profile$HeaderGrp$Info$txt_Age*GEOM*TEXT SET " + "T20 MUMBAI SEASON 3" + "\0", print_writers);
//			}

			
 	
   }
   
   
      private void populateProfile(PrintWriter print_writer,MatchAllData match,int inn,String cat) throws InterruptedException {
    	    	  
    	  
   // 	  DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " + cat.toUpperCase());
    	  
//    		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " +
//    			    (cat.equalsIgnoreCase("MEN") ? "MENS" : "WOMENS"));
    	  
    	  int rowId = 0;
    	  for(BattingCard bc : inning.getBattingCard()) {
    		  rowId = rowId + 1;
			  if(bc.getPlayerId() == player.getPlayerId()) {
				DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 102-PlayerName01 "+  bc.getPlayer().getTicker_name());
				DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 101-layerImage01 "+ 
				"C:\\\\Images\\\\T20_MUMBAI\\\\PHOTOS\\"
		      			+ cat +  "\\\\STRAIGHT_1024\\\\"   +  inning.getBatting_team().getTeamName4()+ "\\\\" +
						bc.getPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION);
			      DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 101-AT "+  rowId);	
			      
			//      DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-TEAMNAME "+  inning.getBatting_team().getTeamBadge());
			   }
    	  	}
			
    	  if(whichprofile.equalsIgnoreCase("MT20 SEASON 3")) {
    		  DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 003-MATCH-NUMBER "+  "MT20 SEASON 3");
    	  }else if(whichprofile.equalsIgnoreCase("WPL")) {
    		  DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 003-MATCH-NUMBER "+  "WPL CAREER");
    	  } else {
    		  DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 003-MATCH-NUMBER "+  "T20 MUMBAI CAREER");
    	  }
    	  
    	  
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$InAt_Profile$Profile$HeaderGrp$Info$txt_BattingHand*GEOM*TEXT SET " + player.getBattingStyle() + "\0", print_writers);
//			if(WhichProfile.equalsIgnoreCase("DT20")) {
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$InAt_Profile$Profile$HeaderGrp$Info$txt_Age*GEOM*TEXT SET " + "T20 CAREER" + "\0", print_writers);
//			}else if(WhichProfile.equalsIgnoreCase("IT20")){
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$InAt_Profile$Profile$HeaderGrp$Info$txt_Age*GEOM*TEXT SET " + "T20I CAREER" + "\0", print_writers);
//			}else if(WhichProfile.equalsIgnoreCase("T20 MUMBAI")){
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$InAt_Profile$Profile$HeaderGrp$Info$txt_Age*GEOM*TEXT SET " + "T20 MUMBAI CAREER" + "\0", print_writers);
//			}else if(WhichProfile.equalsIgnoreCase("IPL")){
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$InAt_Profile$Profile$HeaderGrp$Info$txt_Age*GEOM*TEXT SET " + "IPL CAREER" + "\0", print_writers);
//			}else if(WhichProfile.equalsIgnoreCase("IPL 2026")){
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$InAt_Profile$Profile$HeaderGrp$Info$txt_Age*GEOM*TEXT SET " + "IPL 2026" + "\0", print_writers);
//			}else if(WhichProfile.equalsIgnoreCase("MT20 SEASON 3")){
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$InAt_Profile$Profile$HeaderGrp$Info$txt_Age*GEOM*TEXT SET " + "T20 MUMBAI SEASON 3" + "\0", print_writers);
//			}

			DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 103-StatHead01 "+  "MATCHES");
			
			DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 103-StatValue01 "+  (stat.getMatches() != 0 ? stat.getMatches() : "-"));
			
			DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 104-StatHead02 "+  "RUNS");
			
			DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 104-StatValue02 "+  (stat.getRuns() != 0 ? stat.getRuns() : "-"));	
			
			DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 105-StatHead03 "+  "STRIKE RATE");
			
			DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 105-StatValue03 "+  CricketFunctions.generateStrikeRate(stat.getRuns(), stat.getBalls_faced(), 0));
			
			
    	
      }
      
      private void populateProfileAuction(PrintWriter print_writer,MatchAllData match,int inn) throws InterruptedException {
    	  
     	 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update NAME "+  player.getTicker_name());
    	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update IMAGE "+ "C:\\\\Images\\\\ISPL\\\\Action_Images\\"
       			+ team.getTeamBadge().toUpperCase().substring(0, 3) + "_" +player.getPhoto()+ CricketUtil.PNG_EXTENSION);
    	 	
    	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-TeamRefName " + team.getTeamBadge() );
    	 	TimeUnit.MILLISECONDS.sleep(500);
    	    DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 201-SELECT-IMPACT 0");
     	  
     	  
     	  if(debut.equalsIgnoreCase("YES")) {
     			 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 105-SELECT-DATA-TYPE 0");
     			 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-HEAD "+ "" );
     			 if(!CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn, player.getPlayerId()).isEmpty()) {
      				switch(CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn ,player.getPlayerId())) {
      				case "IMP_IN":
      					DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 201-SELECT-IMPACT 1");
      					break;
      				}
      			}
     	  }else {
     		  DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 105-SELECT-DATA-TYPE 1");
     		  DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-HEAD "+ profile );
     	 	 	TimeUnit.MILLISECONDS.sleep(500);
     	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATHEAD 0" +  " MTS");
     	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATHEAD 1" +  " RUNS");
     	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATHEAD 2" +  " SR");
     	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATHEAD 3" +  " BEST");
     	 	 	TimeUnit.MILLISECONDS.sleep(500);
     	 	 	
     	 	 	if(!CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn, player.getPlayerId()).isEmpty()) {
     				switch(CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn ,player.getPlayerId())) {
     				case "IMP_IN":
     					DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 201-SELECT-IMPACT 1");
     					break;
     				}
     			}
     	 	 	if(profile.equalsIgnoreCase("ISPL SEASON 3")) {
     	 	 		DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 0 " +  (tournament.getMatches() != 0?String.valueOf(tournament.getMatches()):"-"));
     	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 1 " +  (tournament.getRuns() != 0?String.valueOf(tournament.getRuns()):"-"));
     	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 2 " +  CricketFunctions.generateStrikeRate(tournament.getRuns(), tournament.getBallsFaced(), 0));
     	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 3 " +  best);
     	 	 	 	TimeUnit.MILLISECONDS.sleep(500);
     	 	 	}else {
     	 	 		DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 0 " +  stat.getMatches());
     	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 1 " +  runs);
     	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 2 " +  strikeRate);
     	 	 	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 3 " +  best);
     	 	 	 	TimeUnit.MILLISECONDS.sleep(500);
     	 	 	}
     	  }
     }
      
      private void populateBallProfileAuction(PrintWriter print_writer,MatchAllData match,int inn) throws InterruptedException {
     		
    	 //imagetobecorrected 
    	  DoadWriteToTrio(print_writer, "tabfield:set_value_no_update NAME "+  player.getTicker_name());
     	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update IMAGE "+ "C:\\\\Images\\\\ISPL\\\\Action_Images\\"
        			+ team.getTeamBadge().toUpperCase().substring(0, 3) + "_" +player.getPhoto()+ CricketUtil.PNG_EXTENSION);
     	 	TimeUnit.MILLISECONDS.sleep(500);
     	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-TeamRefName " + team.getTeamBadge() );
     	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 201-SELECT-IMPACT 0");
    	  if(debut3.equalsIgnoreCase("YES")) {
    		  DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 105-SELECT-DATA-TYPE 0");
 			 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-HEAD "+ "" );
 			if(!CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn, player.getPlayerId()).isEmpty()) {
				switch(CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn ,player.getPlayerId())) {
				case "IMP_IN":
					DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 201-SELECT-IMPACT 1");
					break;
				}
			}
    	  }else {
    		  DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 105-SELECT-DATA-TYPE 1");
    		  DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-HEAD "+ profile );
    		  TimeUnit.MILLISECONDS.sleep(500);
    	   	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATHEAD 0" +  " MTS");
    	   	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATHEAD 1" +  " WKTS");
    	   	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATHEAD 2" +  " ECON");
    	   	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATHEAD 3" +  " BEST");
    	   	 	TimeUnit.MILLISECONDS.sleep(500);
    	   	 	
    		 	
    		 	if(!CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn, player.getPlayerId()).isEmpty()) {
    				switch(CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn ,player.getPlayerId())) {
    				case "IMP_IN":
    					DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 201-SELECT-IMPACT 1");
    					break;
    				}
    			}
    		 	TimeUnit.MILLISECONDS.sleep(500);
    	   	 if(profile.equalsIgnoreCase("ISPL SEASON 3")) {
    	   		DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 0 " +  (tournament.getMatches() != 0?String.valueOf(tournament.getMatches()):"-"));
    	   	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 1 " +  (tournament.getWickets() != 0?String.valueOf(tournament.getWickets()):"-"));
    	   	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 2 " +  CricketFunctions.getEconomy(tournament.getRunsConceded(), tournament.getBallsBowled(), 2, "-"));
    	   	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 3 " +  best);
    	   	 	TimeUnit.MILLISECONDS.sleep(500);
    	   	 }else {
    	   		DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 0 " +  stat.getMatches());
    	   	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 1 " +  stat.getWickets());
    	   	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 2 " +  economy);
    	   	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 3 " +  best);
    	   	 	TimeUnit.MILLISECONDS.sleep(500);
    	   	 }
    	  }
   		}
     
      private void populateBallProfile(PrintWriter print_writer,MatchAllData match,int inn) throws InterruptedException {
   		
    	  DoadWriteToTrio(print_writer, "tabfield:set_value_no_update NAME "+  player.getTicker_name());
     	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update IMAGE "+ "C:\\\\Images\\\\ISPL\\\\PHOTOS\\"
        			+ team.getTeamName4()+ "\\\\STRAIGHT_1024\\\\" +player.getPhoto()+ CricketUtil.PNG_EXTENSION);
     	 	TimeUnit.MILLISECONDS.sleep(500);
     	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-TeamRefName " + team.getTeamBadge() );
     	 	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 201-SELECT-IMPACT 0");
    	  if(debut3.equalsIgnoreCase("YES")) {
    		  DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 105-SELECT-DATA-TYPE 0");
 			 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-HEAD "+ "" );
 			if(!CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn, player.getPlayerId()).isEmpty()) {
				switch(CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn ,player.getPlayerId())) {
				case "IMP_IN":
					DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 201-SELECT-IMPACT 1");
					break;
				}
			}
    	  }else {
    		  DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 105-SELECT-DATA-TYPE 1");
    		  DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-HEAD "+ profile );
    		  TimeUnit.MILLISECONDS.sleep(500);
    	   	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATHEAD 0" +  " MTS");
    	   	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATHEAD 1" +  " WKTS");
    	   	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATHEAD 2" +  " ECON");
    	   	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATHEAD 3" +  " BEST");
    	   	 	TimeUnit.MILLISECONDS.sleep(500);
    	   	 	
    		 	
    		 	if(!CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn, player.getPlayerId()).isEmpty()) {
    				switch(CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn ,player.getPlayerId())) {
    				case "IMP_IN":
    					DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 201-SELECT-IMPACT 1");
    					break;
    				}
    			}
    		 	TimeUnit.MILLISECONDS.sleep(500);
    	   	 if(profile.equalsIgnoreCase("ISPL SEASON 3")) {
    	   		DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 0 " +  (tournament.getMatches() != 0?String.valueOf(tournament.getMatches()):"-"));
    	   	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 1 " +  (tournament.getWickets() != 0?String.valueOf(tournament.getWickets()):"-"));
    	   	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 2 " +  CricketFunctions.getEconomy(tournament.getRunsConceded(), tournament.getBallsBowled(), 2, "-"));
    	   	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 3 " +  best);
    	   	 	TimeUnit.MILLISECONDS.sleep(500);
    	   	 }else {
    	   		DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 0 " +  stat.getMatches());
    	   	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 1 " +  stat.getWickets());
    	   	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 2 " +  economy);
    	   	 	DoadWriteToTrio(print_writer, "table:set_cell_value 103-DATA-ALL STATVALUE 3 " +  best);
    	   	 	TimeUnit.MILLISECONDS.sleep(500);
    	   	 }
    	  }
   		}
    
      private void populateDoublematchid(PrintWriter print_writer,MatchAllData match,CricketService cricketService , String day) {
    	 int row_id = 1;
    	 String Date = "";
    	 Calendar cal = Calendar.getInstance();
 		if(day.toUpperCase().equalsIgnoreCase("Today")) {
			Date =  new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime());
		}
		else if(day.toUpperCase().equalsIgnoreCase("Tomorrow")) {
			cal.add(Calendar.DATE, +1);

		}
		for(int i = 0; i <= cricketService.getFixtures().size()-1; i++) {
			if(cricketService.getFixtures().get(i).getDate().equalsIgnoreCase(Date)) {
				DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-MATCH-NUMBER"+row_id + " "+  "MATCH " + cricketService.getFixtures().get(i).getMatchnumber());
				
				if(row_id == 1 ) {
					DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-TEAM-REF-NAME-1 " + 
							cricketService.getTeams().get(cricketService.getFixtures().get(i).getHometeamid()-1).getTeamBadge());	
					DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 002-TEAM-REF-NAME-2 "+ 
							cricketService.getTeams().get(cricketService.getFixtures().get(i).getAwayteamid()-1).getTeamBadge());		
				}else if(row_id == 2 ) {
					DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 003-TEAM-REF-NAME-3 " + 
							cricketService.getTeams().get(cricketService.getFixtures().get(i).getHometeamid()-1).getTeamBadge());	
					DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 004-TEAM-REF-NAME-4 "+ 
							cricketService.getTeams().get(cricketService.getFixtures().get(i).getAwayteamid()-1).getTeamBadge());		
				}
				
				
				row_id = row_id+1;
			}
		}
 	}
    
//  private void populateBoundaries(PrintWriter print_writer,MatchAllData match,CricketService cricketService,int innn,String cat) {
//	
//	  
//	//  DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " + cat.toUpperCase());
//	  
////		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " +
////			    (cat.equalsIgnoreCase("MEN") ? "MENS" : "WOMENS"));
//	  for(Inning inn : match.getMatch().getInning()) {
//	//	if(inn.getInningNumber() == (innn-1)) {
//		  if(inn.getInningNumber() == (innn)) {
//			  
//	//		 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-TEAMNAME "+  inn.getBatting_team().getTeamBadge());
//			 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 101-RUNS "+ inn.getTotalFours());
//			 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 103-BALLS "+  inn.getTotalSixes());
//			 
//			 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 003-Header "+  "BOUNDARIES");
//			 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 100-HEADER "+  "FOUR" + CricketFunctions.Plural(inn.getTotalFours()).toUpperCase());
//			 DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 102-HEADER "+  "SIXES" );
//			 
//		}
//	}
// 		
//    
// 	}
  
//      private void populateBoundaries(PrintWriter print_writer, MatchAllData match, CricketService cricketService, int innn) {
//    	  //  StringBuilder batchCommands = new StringBuilder();
//    	    
//    	    for (Inning inn : match.getMatch().getInning()) {
//    	        if (inn.getInningNumber() == (innn - 1)) {
////    	            batchCommands
////    	                .append("tabfield:set_value_no_update 001-TEAM-REF-NAME-1 ")
////    	                .append(inn.getBatting_team().getTeamBadge())
////    	                .append(return_key).append(line_feed)
////    	                
////    	                .append("tabfield:set_value_no_update 002-TEAM-REF-NAME-2 ")
////    	                .append(inn.getBatting_team().getTeamBadge())
////    	                .append(return_key).append(line_feed)
////    	                
////    	                .append("tabfield:set_value_no_update 107-TEXT ")
////    	                .append("FOUR").append(CricketFunctions.Plural(inn.getTotalFours()).toUpperCase())
////    	                .append(": ").append(inn.getTotalFours())
////    	                .append(return_key).append(line_feed)
////    	                
////    	                .append("tabfield:set_value_no_update 108-TEXT ")
////    	                .append("SIXES: ").append(inn.getTotalSixes())
////    	                .append(return_key).append(line_feed);
////    	            
//    	            DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-TEAMNAME "+  inn.getBatting_team().getTeamBadge());
//
//    	     		//run
//    	     		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 101-RUNS "+ inn.getTotalFours());
//    	     		
//    	     	//	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 006-DATA02 BALL"+ CricketFunctions.Plural(CricketFunctions.GetTargetData(match).getRemaningBall()).toUpperCase());
//    	     		
//    	     		//balls
//    	     		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 102-BALLS "+  inn.getTotalSixes());
//    	     //		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 003-DATA02 RUN"+ CricketFunctions.Plural(CricketFunctions.GetTargetData(match).getRemaningRuns()).toUpperCase());
//    	     	
//    	            
//    	            break; // ← Add this! No need to keep looping after match found
//    	        }
//    	    }
//    	    
//    	}
  
  
  
  
 private void populateMVP(PrintWriter print_writer,MatchAllData match,CricketService cricketService) {
 		
 		
    	List<mvp_leaderBoard.Player> finalFivePlayers = new ArrayList<>();
    	
    	//topdatalist
    	for (int i = 0; i < mvp.getData().getTop().size(); i++) {
    	    finalFivePlayers.add(mvp.getData().getTop().get(i));
    	}
    	for (int i = 0; i < 2 && i < mvp.getData().getList().size(); i++) {
    	    finalFivePlayers.add(mvp.getData().getList().get(i));
    	}

    	for (int j = 0; j < finalFivePlayers.size(); j++) {
    		String player_id = finalFivePlayers.get(j).getPlayerId();
    		
    		
    		DoadWriteToTrio(print_writer, "table:set_cell_value 001-PLAYERS POINTS "+ j + " "+ finalFivePlayers.get(j).getFinalPoints());
    		DoadWriteToTrio(print_writer, "table:set_cell_value 001-PLAYERS RANK "+ j + " " +  (j+1));
    		
    		player = cricketService.getAllPlayer().stream().filter(plyr -> Long.valueOf(player_id).equals(plyr.getOnlineId())).findAny().orElse(null);
    		
    		team = cricketService.getTeams().stream().filter(tm -> tm.getTeamId() == player.getTeamId()).findAny().orElse(null);

    		
    		DoadWriteToTrio(print_writer, "table:set_cell_value 001-PLAYERS NAME "+ j + " "+ player.getTicker_name());
    		
    		
    		DoadWriteToTrio(print_writer, "table:set_cell_value 001-PLAYERS IMAGE "+ j + " "+ "C:\\\\Images\\\\ISPL\\\\PHOTOS\\"
        			+ team.getTeamName4()+ "\\\\STRAIGHT_1024\\\\" +player.getPhoto()+ CricketUtil.PNG_EXTENSION);
    	}
 	}
 	private String populateFixture(PrintWriter print_writer,MatchAllData match, CricketService cricketService, int teamId) {
 		
 		FixturesList.clear();
		for(Fixture fixture : CricketFunctions.processAllFixtures(cricketService)) {
			if(fixture.getHometeamid() == Integer.valueOf(teamId) || 
					fixture.getAwayteamid() == Integer.valueOf(teamId)) {
				FixturesList.add(fixture);
			}
		}
		
		if(FixturesList == null) {
			return "populateFixturesAndResults : FixturesList is returning NULL";
		}
		
		team = cricketService.getTeams().stream().filter(tm -> tm.getTeamId() == Integer.valueOf(teamId)).findAny().orElse(null);
		if(team == null) {
			return "populatePlayingXI: Team id [" + Integer.valueOf(teamId) + "] from database is returning NULL";
		}
		
 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-HEADER1 " + "");
 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-HEADER2 " + "FIXTURES & RESULTS");
 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update  001-SELECT-HEADER-STYLE " + "0");
 		
 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-LEFTLOGO " + "IMAGE*Default/Essentials/Logos/TLogo");
 		
 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-TEAM-LOGO " + "IMAGE*Default/Essentials/Logos_BW/" + team.getTeamBadge() + "");
 		
 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update   004-NumberOfMathes " + (FixturesList.size()-1));
 		
 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-SUB-HEADER " + match.getSetup().getTournament());
 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-SUB-HEADER02 " + team.getTeamName1());
 		
 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 002-STAT-HEAD-NAME " + "");
 		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 003-STAT-VALUE-NAME-1 " + "DATE/RESULTS");
 		
 		Calendar cal_npl = Calendar.getInstance();
		cal_npl.add(Calendar.DATE, 0);
		
		for(int i=0;i<= FixturesList.size()-1;i++) {
			 
			DoadWriteToTrio(print_writer, "table:set_cell_value 003-LEADER-BOARD-DATA 001-MATCH " + i + " " + FixturesList.get(i).getMatchfilename());
			 
			DoadWriteToTrio(print_writer, "table:set_cell_value 003-LEADER-BOARD-DATA 002-TeamName " + i + " " + (team.getTeamId() == FixturesList.get(i).getHometeamid() ? FixturesList.get(i).getAway_Team().getTeamName1() 
					: FixturesList.get(i).getHome_Team().getTeamName1()));
			
			DoadWriteToTrio(print_writer, "table:set_cell_value 003-LEADER-BOARD-DATA 003-VS " + i + " v");
			 
			if(FixturesList.get(i).getMargin() != null && !FixturesList.get(i).getMargin().isEmpty()) {
				if(FixturesList.get(i).getWinnerteam() != null && !FixturesList.get(i).getWinnerteam().isEmpty()) {
					if(FixturesList.get(i).getWinnerteam().equalsIgnoreCase(team.getTeamName1())) {
						DoadWriteToTrio(print_writer, "table:set_cell_value  003-LEADER-BOARD-DATA 004-RESULT " + i + 
								" WON BY " + FixturesList.get(i).getMargin());
					}else {
						DoadWriteToTrio(print_writer, "table:set_cell_value 003-LEADER-BOARD-DATA 004-RESULT " + i + 
								" LOST BY " + FixturesList.get(i).getMargin());
					}
				}else {
					DoadWriteToTrio(print_writer, "table:set_cell_value 003-LEADER-BOARD-DATA 004-RESULT " + i + " " +
							FixturesList.get(i).getMargin());
				}
			}else {
				
				if(FixturesList.get(i).getDate().equalsIgnoreCase(new SimpleDateFormat("dd-MM-yyyy").format(cal_npl.getTime()))) {
					DoadWriteToTrio(print_writer, "table:set_cell_value 003-LEADER-BOARD-DATA 004-RESULT " + i + 
							" TODAY");
				}else {
					DoadWriteToTrio(print_writer, "table:set_cell_value 003-LEADER-BOARD-DATA 004-RESULT " + i + " " +
							CricketFunctions.ordinal(Integer.valueOf(FixturesList.get(i).getDate().split("-")[0]))
					+ " " + Month.of(Integer.valueOf(FixturesList.get(i).getDate().split("-")[1])));
				}
			}
		}
		
		return null;
	}
 	private void sendVizMSENextToBatPage(String pageName,
            MatchAllData match,
            CricketService cricketService,
            String cat,
            String ipAddress,
            String showName) {

		String[] urls = getShowIdAndURL(
		pageName,
		match,
		ipAddress,
		"Next3",
		showName);
		
		String modelUrl = urls[0];
		String putUrl = urls[1];
		
		Inning inning = match.getMatch().getInning()
		.stream()
		.filter(inn -> CricketUtil.YES.equalsIgnoreCase(inn.getIsCurrentInning()))
		.findAny()
		.orElse(null);
		
		if (inning == null) {
		return;
		}
		
		StringBuilder payloadXml = new StringBuilder();
		
		payloadXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		payloadXml.append("<payload xmlns=\"http://www.vizrt.com/types\" ");
		payloadXml.append("model=\"").append(modelUrl).append("\">\n");
		
		payloadXml.append("<field name=\"002-HEADER\"><value>NEXT TO BAT</value></field>\n");
		
		int rowId = 0;
		int position = 0;
		
		for (BattingCard bc : inning.getBattingCard()) {
		
		position++;
		
		if (rowId >= 3) {
		break;
		}
		
		if (CricketUtil.STILL_TO_BAT.equalsIgnoreCase(bc.getStatus())) {
		
		if (bc.getHowOut() != null &&
		!bc.getHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
		continue;
		}
		
		rowId++;
		
		String imagePath =
		"C:\\Images\\T20_MUMBAI\\PHOTOS\\"
		+ cat
		+ "\\STRAIGHT_1024\\"
		+ (inning.getBattingTeamId() == match.getSetup().getHomeTeamId()
		? match.getSetup().getHomeTeam().getTeamName4()
		: match.getSetup().getAwayTeam().getTeamName4())
		+ "\\"
		+ bc.getPlayer().getPhoto()
		+ CricketUtil.PNG_EXTENSION;
		
		payloadXml.append("<field name=\"")
		.append(rowId)
		.append("02-PLAYER-IMAGE\"><value>")
		.append(imagePath)
		.append("</value></field>\n");
		
		payloadXml.append("<field name=\"")
		.append(rowId)
		.append("03-PLAYER-NAME\"><value>")
		.append(" ")
		.append(bc.getPlayer().getTicker_name())
		.append("</value></field>\n");
		}
		}
		
		payloadXml.append("</payload>");
		
		putMethod(putUrl, payloadXml.toString());
		}
 	
//	private void populateNextToBat(PrintWriter print_writer, MatchAllData match, CricketService cricketService,String cat) {
//		
//		inning = match.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
//		
//		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 002-HEADER " + "NEXT TO BAT");
////		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 001-TEAMNAME " + (match.getSetup().getHomeTeamId() == inning.getBattingTeamId() ? 
////				match.getSetup().getHomeTeam().getTeamBadge() : match.getSetup().getAwayTeam().getTeamBadge()));
//		
//	//	DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " + cat.toUpperCase());
//		
////		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-EVENTNAME " +
////			    (cat.equalsIgnoreCase("MEN") ? "MENS" : "WOMENS"));
//		int rowId = 0;
//		int position = 0;
//		for(BattingCard bc : inning.getBattingCard()) {
//			position++;
//			if(rowId>=3) break;
//			switch (bc.getStatus()) {
//			case CricketUtil.STILL_TO_BAT:
//				if(bc.getHowOut() != null && !bc.getHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) continue;
//				rowId++;
////							DoadWriteToTrio(print_writer,  "tabfield:set_value_no_update " + rowId + "01-IN-AT-VALUE " +  position);
//
//
//				DoadWriteToTrio(print_writer, "tabfield:set_value_no_update " + rowId + "02-PLAYER-IMAGE " + 
//	    				" C:\\\\Images\\\\T20_MUMBAI\\\\PHOTOS\\"
//	    		      			+ cat +  "\\\\STRAIGHT_1024\\\\"   +  (inning.getBattingTeamId()== match.getSetup().getHomeTeamId() ?match.getSetup().getHomeTeam().getTeamName4() : 
//	    							match.getSetup().getAwayTeam().getTeamName4())+ "\\\\" +
//	    							bc.getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION);
//				
//				DoadWriteToTrio(print_writer,  "tabfield:set_value_no_update " + rowId + "03-PLAYER-NAME " +" "+ bc.getPlayer().getTicker_name());
//				
//				
//				break;
//			}
//			
//		}
//	}
	private void populateUPLINE(PrintWriter print_writer,MatchAllData match,CricketService cricketService) {
 		
 		for(Inning inn:match.getMatch().getInning()) {
 			if(inn.getIsCurrentInning().equalsIgnoreCase("YES")) {
 				DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 000-TEAM-REF-NAME " + inn.getBatting_team().getTeamBadge());
					
					DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 301-HEADER " + match.getSetup().getMatchIdent());
					int rowId = 0;
 				for(BattingCard bc : inn.getBattingCard()) {
 					rowId++;
 					DoadWriteToTrio(print_writer,  "table:set_cell_value 302-PLAYERS-DATA 10-NAME " +(rowId-1)+" "+ bc.getPlayer().getTicker_name() + "");
 					
 					DoadWriteToTrio(print_writer, "table:set_cell_value 302-PLAYERS-DATA 01-IMAGE " + (rowId-1) + " C:\\\\Images\\\\ISPL\\\\PHOTOS\\"
 			    			+ inn.getBatting_team().getTeamBadge() + "\\\\STRAIGHT_1024\\\\" + bc.getPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION);
 					
 					if(bc.getPlayer().getRole().equalsIgnoreCase(CricketUtil.BATSMAN) || bc.getPlayer().getRole().equalsIgnoreCase("BAT/KEEPER")) {
						if(bc.getPlayer().getBattingStyle().equalsIgnoreCase("RHB")) {
							DoadWriteToTrio(print_writer, "table:set_cell_value 302-PLAYERS-DATA 02-ROLE " + (rowId - 1) + " IMAGE*Default/Essentials/Icons/Batsman_RightHand");
						}else if(bc.getPlayer().getBattingStyle().equalsIgnoreCase("LHB")) {
							DoadWriteToTrio(print_writer, "table:set_cell_value 302-PLAYERS-DATA 02-ROLE " + (rowId - 1) + " IMAGE*Default/Essentials/Icons/Batsman_Lefthand");
						}
					}
					else if(bc.getPlayer().getRole().equalsIgnoreCase(CricketUtil.BOWLER)) {
						if(bc.getPlayer().getBowlingStyle() == null) {
							DoadWriteToTrio(print_writer, "table:set_cell_value 302-PLAYERS-DATA 02-ROLE " + (rowId - 1) + " IMAGE*Default/Essentials/Icons/Pace_Bowler");
						}else {
							switch(bc.getPlayer().getBowlingStyle()) {
							case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
								DoadWriteToTrio(print_writer, "table:set_cell_value 302-PLAYERS-DATA 02-ROLE " + (rowId - 1) + " IMAGE*Default/Essentials/Icons/Pace_Bowler");
								break;
							case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
								DoadWriteToTrio(print_writer, "table:set_cell_value 302-PLAYERS-DATA 02-ROLE " + (rowId - 1) + " IMAGE*Default/Essentials/Icons/Off_Spinner");
								break;
							}
						}
					}
					else if(bc.getPlayer().getRole().equalsIgnoreCase("ALL-ROUNDER")) {
						if(bc.getPlayer().getBowlingStyle() == null) {
							if(bc.getPlayer().getBattingStyle().equalsIgnoreCase("RHB")) {
								DoadWriteToTrio(print_writer, "table:set_cell_value 302-PLAYERS-DATA 02-ROLE " + (rowId - 1) + " IMAGE*Default/Essentials/Icons/Pace_BowlerAllrounerRightHand");
							}else if(bc.getPlayer().getBattingStyle().equalsIgnoreCase("LHB")) {
								DoadWriteToTrio(print_writer, "table:set_cell_value 302-PLAYERS-DATA 02-ROLE " + (rowId - 1) + " IMAGE*Default/Essentials/Icons/Pace_BowlerAllrounerLeftHand");
							}
						}else {
							switch(bc.getPlayer().getBowlingStyle()) {
							case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
								if(bc.getPlayer().getBattingStyle().equalsIgnoreCase("RHB")) {
									DoadWriteToTrio(print_writer, "table:set_cell_value 302-PLAYERS-DATA 02-ROLE " + (rowId - 1) + " IMAGE*Default/Essentials/Icons/Pace_BowlerAllrounerRightHand");
								}else if(bc.getPlayer().getBattingStyle().equalsIgnoreCase("LHB")) {
									DoadWriteToTrio(print_writer, "table:set_cell_value 302-PLAYERS-DATA 02-ROLE " + (rowId - 1) + " IMAGE*Default/Essentials/Icons/Pace_BowlerAllrounerLeftHand");
								}
								break;
							case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
								if(bc.getPlayer().getBattingStyle().equalsIgnoreCase("RHB")) {
									DoadWriteToTrio(print_writer, "table:set_cell_value 302-PLAYERS-DATA 02-ROLE " + (rowId - 1) + " IMAGE*Default/Essentials/Icons/Off_SpinnerAllrounderRightHand");
								}else if(bc.getPlayer().getBattingStyle().equalsIgnoreCase("LHB")) {
									DoadWriteToTrio(print_writer, "table:set_cell_value 302-PLAYERS-DATA 02-ROLE " + (rowId - 1) + " IMAGE*Default/Essentials/Icons/Off_SpinnerAllrounderLeftHand");
								}
								break;
							}
						}
					}
 					DoadWriteToTrio(print_writer,  "table:set_cell_value 302-PLAYERS-DATA 05-SELECT-IMPACT " +(rowId-1)+" "+ "0" + "");
 					if(!CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn.getInningNumber(), bc.getPlayerId()).isEmpty()) {
 						switch(CricketFunctions.checkBatAndBallImpactInOutPlayerISPL(match.getEventFile().getEvents(), inn.getInningNumber() ,bc.getPlayerId())) {
 						case "IMP_IN":
 							DoadWriteToTrio(print_writer,  "table:set_cell_value 302-PLAYERS-DATA 05-SELECT-IMPACT " +(rowId-1)+" "+ "1" + "");
 							break;
 						case "IMP_OUT":
							if(bc.getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
								if(bc.getBalls() == 0) {
									rowId = rowId - 1;
								}
							}
							break;
 						}
 					}
 					
 					switch (bc.getStatus()) {
					case CricketUtil.STILL_TO_BAT:
						DoadWriteToTrio(print_writer,  "table:set_cell_value 302-PLAYERS-DATA 03-IN-AT-HEADER " +(rowId-1)+" "+ "IN AT" + "");
						DoadWriteToTrio(print_writer,  "table:set_cell_value 302-PLAYERS-DATA 04-IN-AT-VALUE " +(rowId-1)+" "+ rowId + "");
						DoadWriteToTrio(print_writer,  "table:set_cell_value 302-PLAYERS-DATA 11-SELECT-HIGHLIGHT " + (rowId-1) + " " + "0" +"");
						DoadWriteToTrio(print_writer,  "table:set_cell_value 302-PLAYERS-DATA 14-RUNS " +(rowId-1)+" "+ "" +"");
						DoadWriteToTrio(print_writer,  "table:set_cell_value 302-PLAYERS-DATA 15-NOT-OUT-STAR " + (rowId-1) + " "+ "" +"");
						DoadWriteToTrio(print_writer,  "table:set_cell_value 302-PLAYERS-DATA 16-BALLS " +(rowId-1)+" "+ ""+"");
						break;
					default:
						DoadWriteToTrio(print_writer,  "table:set_cell_value 302-PLAYERS-DATA 03-IN-AT-HEADER " +(rowId-1)+" "+ "");
						DoadWriteToTrio(print_writer,  "table:set_cell_value 302-PLAYERS-DATA 04-IN-AT-VALUE " +(rowId-1)+" "+ "");
						
						DoadWriteToTrio(print_writer,  "table:set_cell_value 302-PLAYERS-DATA 14-RUNS " +(rowId-1)+" "+ bc.getRuns() +"");
						DoadWriteToTrio(print_writer,  "table:set_cell_value 302-PLAYERS-DATA 15-NOT-OUT-STAR " + (rowId-1) + " " 
								+ (bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)?"*":"") +"");
						DoadWriteToTrio(print_writer,  "table:set_cell_value 302-PLAYERS-DATA 11-SELECT-HIGHLIGHT " + (rowId-1) + " " 
								+ (bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)?"1":"0") +"");
						DoadWriteToTrio(print_writer,  "table:set_cell_value 302-PLAYERS-DATA 16-BALLS " +(rowId-1)+" "+ bc.getBalls()+"");
						
						break;
					}
 				}
 			}
 		}
		
 	}
	
	private void populateLineUp(PrintWriter print_writer, MatchAllData match, CricketService cricketService,Integer TeamId) {
	
		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 002-REF-NAME " + (match.getSetup().getHomeTeamId() == TeamId ? 
				match.getSetup().getHomeTeam().getTeamName4() : match.getSetup().getAwayTeam().getTeamName4()));
		DoadWriteToTrio(print_writer, "tabfield:set_value_no_update 003-REF-NAME " + (match.getSetup().getHomeTeamId() == TeamId ? 
				match.getSetup().getAwayTeam().getTeamName4() : match.getSetup().getHomeTeam().getTeamName4()));
		
		int rowId = 0;
		//team1
		if(TeamId==match.getSetup().getHomeTeamId()) {
			for(Player ply : match.getSetup().getHomeSquad()) {
				rowId++;
				DoadWriteToTrio(print_writer, "table:set_cell_value 004-LIST-1 001-SCORE-POSITION-OMO " +(rowId-1) +" 0");
				DoadWriteToTrio(print_writer, "table:set_cell_value 004-LIST-1 01-PLAYER-IMAGE " +(rowId-1)+ " C:\\\\Images\\\\ISPL\\\\PHOTOS\\"+
				(TeamId== match.getSetup().getHomeTeamId() ?match.getSetup().getHomeTeam().getTeamName4() : 
					match.getSetup().getAwayTeam().getTeamName4() )+ "\\\\"+ply.getPhoto()+CricketUtil.PNG_EXTENSION + "");
				//DoadWriteToTrio(print_writer,  "table:set_cell_value 004-LIST-1 02-ICON " +(rowId-1)+ " IMAGE*/Default/Essentials/Icons/"+getPlayerIconName(ply)+ "");
				DoadWriteToTrio(print_writer,  "table:set_cell_value 004-LIST-1 03-FIRST-NAME " +(rowId-1)+" "+ ply.getFirstname() + "");
				DoadWriteToTrio(print_writer,  "table:set_cell_value 004-LIST-1 04-LAST-NAME " + (rowId-1)+" "+(ply.getSurname() == null ? "" : ply.getSurname()) + "");
				DoadWriteToTrio(print_writer,  "table:set_cell_value 004-LIST-1 05-OPENER " +(rowId-1)+" " + getPlayerRole(ply));
				DoadWriteToTrio(print_writer,  "table:set_cell_value 004-LIST-1 06-POSITION " +(rowId-1)+" ");
				DoadWriteToTrio(print_writer,  "table:set_cell_value 004-LIST-1 07-RUNS "	 + (rowId-1)+"  ");
				DoadWriteToTrio(print_writer,  "table:set_cell_value 004-LIST-1 08-BALLS " +(rowId-1)+"  ");
	            DoadWriteToTrio(print_writer, "table:set_cell_value 004-LIST-1 08-SELECT-CAPTAIN " + (rowId-1)+ " " + 
	            		(ply.getCaptainWicketKeeper() != null && ply.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN) ? "1" : "0"));

			}
		}else {
			for(Player ply : match.getSetup().getAwaySquad()) {
				rowId++;
				DoadWriteToTrio(print_writer, "table:set_cell_value 004-LIST-1 001-SCORE-POSITION-OMO " +(rowId-1) +" 0");
				DoadWriteToTrio(print_writer, "table:set_cell_value 004-LIST-1 01-PLAYER-IMAGE " +(rowId-1)+ " C:\\\\Images\\\\ISPL\\\\PHOTOS\\"+
				(TeamId== match.getSetup().getHomeTeamId() ?match.getSetup().getHomeTeam().getTeamName4() : 
					match.getSetup().getAwayTeam().getTeamName4() )+ "\\\\"+ply.getPhoto()+CricketUtil.PNG_EXTENSION + "");
				//DoadWriteToTrio(print_writer,  "table:set_cell_value 004-LIST-1 02-ICON " +(rowId-1)+ " IMAGE*/Default/Essentials/Icons/"+getPlayerIconName(ply)+ "");
				DoadWriteToTrio(print_writer,  "table:set_cell_value 004-LIST-1 03-FIRST-NAME " +(rowId-1)+" "+ ply.getFirstname() + "");
				DoadWriteToTrio(print_writer,  "table:set_cell_value 004-LIST-1 04-LAST-NAME " + (rowId-1)+" "+(ply.getSurname() == null ? "" : ply.getSurname()) + "");
				DoadWriteToTrio(print_writer,  "table:set_cell_value 004-LIST-1 05-OPENER " +(rowId-1)+" " + getPlayerRole(ply));
				DoadWriteToTrio(print_writer,  "table:set_cell_value 004-LIST-1 06-POSITION " +(rowId-1)+" ");
				DoadWriteToTrio(print_writer,  "table:set_cell_value 004-LIST-1 07-RUNS "	 + (rowId-1)+"  ");
				DoadWriteToTrio(print_writer,  "table:set_cell_value 004-LIST-1 08-BALLS " +(rowId-1)+"  ");
				DoadWriteToTrio(print_writer, "table:set_cell_value 004-LIST-1 08-SELECT-CAPTAIN " + (rowId-1)+ " " + 
		            (ply.getCaptainWicketKeeper() != null && ply.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN) ? "1" : "0"));
			}
		}
		//team2
		rowId = 0;
		if(TeamId == match.getSetup().getHomeTeamId()) {
			for(Player ply : match.getSetup().getAwaySquad()) {
				rowId++;
				DoadWriteToTrio(print_writer, "table:set_cell_value 005-LIST-2 001-SCORE-POSITION-OMO " +(rowId-1) +" 0");
				DoadWriteToTrio(print_writer, "table:set_cell_value 005-LIST-2 01-PLAYER-IMAGE " +(rowId-1)+ " C:\\\\Images\\\\ISPL\\\\PHOTOS\\"+
				(TeamId== match.getSetup().getHomeTeamId() ?match.getSetup().getHomeTeam().getTeamName4() : 
					match.getSetup().getAwayTeam().getTeamName4() )+ "\\\\"+ply.getPhoto()+CricketUtil.PNG_EXTENSION + "");
				//DoadWriteToTrio(print_writer,  "table:set_cell_value 005-LIST-2 02-ICON " +(rowId-1)+ " IMAGE*/Default/Essentials/Icons/"+getPlayerIconName(ply)+ "");
				DoadWriteToTrio(print_writer,  "table:set_cell_value 005-LIST-2 03-FIRST-NAME " +(rowId-1)+" "+ ply.getFirstname() + "");
				DoadWriteToTrio(print_writer,  "table:set_cell_value 005-LIST-2 04-LAST-NAME " + (rowId-1)+" "+(ply.getSurname() == null ? "" : ply.getSurname()) + "");
				DoadWriteToTrio(print_writer,  "table:set_cell_value 005-LIST-2 05-OPENER " +(rowId-1)+" " + getPlayerRole(ply));
				DoadWriteToTrio(print_writer,  "table:set_cell_value 005-LIST-2 06-POSITION " +(rowId-1)+" ");
				DoadWriteToTrio(print_writer,  "table:set_cell_value 005-LIST-2 07-RUNS "	 + (rowId-1)+"  ");
				DoadWriteToTrio(print_writer,  "table:set_cell_value 005-LIST-2 08-BALLS " +(rowId-1)+"  ");
	            DoadWriteToTrio(print_writer, "table:set_cell_value 005-LIST-2 08-SELECT-CAPTAIN " + (rowId-1) + " " 
				+ (ply.getCaptainWicketKeeper() != null && ply.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN) ? "1" : "0"));

			}
		}else {
			for(Player ply : match.getSetup().getHomeSquad()) {
				rowId++;
				DoadWriteToTrio(print_writer, "table:set_cell_value 005-LIST-2 001-SCORE-POSITION-OMO " +(rowId-1) +" 0");
				DoadWriteToTrio(print_writer, "table:set_cell_value 005-LIST-2 01-PLAYER-IMAGE " +(rowId-1)+ " C:\\\\Images\\\\ISPL\\\\PHOTOS\\"+
				(TeamId== match.getSetup().getHomeTeamId() ?match.getSetup().getHomeTeam().getTeamName4() : 
					match.getSetup().getAwayTeam().getTeamName4() )+ "\\\\"+ply.getPhoto()+CricketUtil.PNG_EXTENSION + "");
				//DoadWriteToTrio(print_writer,  "table:set_cell_value 005-LIST-2 02-ICON " +(rowId-1)+ " IMAGE*/Default/Essentials/Icons/"+getPlayerIconName(ply)+ "");
				DoadWriteToTrio(print_writer,  "table:set_cell_value 005-LIST-2 03-FIRST-NAME " +(rowId-1)+" "+ ply.getFirstname() + "");
				DoadWriteToTrio(print_writer,  "table:set_cell_value 005-LIST-2 04-LAST-NAME " + (rowId-1)+" "+(ply.getSurname() == null ? "" : ply.getSurname()) + "");
				DoadWriteToTrio(print_writer,  "table:set_cell_value 005-LIST-2 05-OPENER " +(rowId-1)+" " + getPlayerRole(ply));
				DoadWriteToTrio(print_writer,  "table:set_cell_value 005-LIST-2 06-POSITION " +(rowId-1)+" ");
				DoadWriteToTrio(print_writer,  "table:set_cell_value 005-LIST-2 07-RUNS "	 + (rowId-1)+"  ");
				DoadWriteToTrio(print_writer,  "table:set_cell_value 005-LIST-2 08-BALLS " +(rowId-1)+"  ");
	            DoadWriteToTrio(print_writer, "table:set_cell_value 005-LIST-2 08-SELECT-CAPTAIN " + (rowId-1) + " " 
				+ (ply.getCaptainWicketKeeper() != null && ply.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN) ? "1" : "0"));

			}
		}
	}
	public static String getPlayerRole(Player as) {
		switch (as.getRole().toUpperCase()) {
		case "BATSMAN":case "BATTER":
			return "BAT";
		case "BOWLER":
			return "BOWL";
		default:
			return as.getRole().toUpperCase();
		}
	}
	public static String getPlayerIconName(Player as) {
	    String playerIcon = "";
	    
	    if (as.getRole().equalsIgnoreCase("BATSMAN")) {
	        if (as.getBattingStyle().equalsIgnoreCase("RHB")) {
	            playerIcon = "Batsman_RightHand";
	        } else if (as.getBattingStyle().equalsIgnoreCase("LHB")) {
	            playerIcon = "Batsman_LeftHand";
	        }
	    } else if (as.getRole().equalsIgnoreCase("BOWLER")) {
	        if (as.getBowlingStyle() == null) {
	            playerIcon = "Pace_Bowler";
	        } else {
	            switch (as.getBowlingStyle()) {
	                case "RF": case "RFM": case "RMF": case "RM": case "RSM":
	                case "LF": case "LFM": case "LMF": case "LM":
	                    playerIcon = "Pace_Bowler";
	                    break;
	                case "ROB": case "RLB": case "LSL": case "WSL":
	                case "LCH": case "RLG": case "WSR": case "LSO":
	                    playerIcon = "Off_Spinner";
	                    break;
	            }
	        }
	    } else if (as.getRole().equalsIgnoreCase("ALL-ROUNDER")) {
	    	 if (as.getBattingStyle().equalsIgnoreCase("RHB")) {
		            playerIcon = "All_RounderRightHand";
		        } else if (as.getBattingStyle().equalsIgnoreCase("LHB")) {
		            playerIcon = "All_RounderLeftHand";
		        }
	    }
	     if(as.getCaptainWicketKeeper()!=null && (as.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")||
	    		 as.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER))) {
			 playerIcon = "WicketKeeper";
		}

	    return playerIcon;  
	}
	
//	private void sendVizMSEBoundaryPage(
//	        String pageName,
//	        MatchAllData match,
//	        int inningNumber) {
//
//	   // String MSE_URL = "http://10.100.2.1:8580";
//	    
//	    	String MSE_URL = "http://10.100.2.1:8580/models/vdf/storage/shows/%7B25B3F0C6-1D65-4DFC-89C8-8ECFB75EFC1A%7D/mastertemplates/RunsAndBall";
//	    String totalFours  = "0";
//	    String totalSixes  = "0";
//	    String fourHeader  = "FOURS";
//	    String teamBadge   = "";
//
//	    for (Inning inn : match.getMatch().getInning()) {
//	        if (inn.getInningNumber() == inningNumber) {
//	            totalFours = String.valueOf(inn.getTotalFours());
//	            totalSixes = String.valueOf(inn.getTotalSixes());
//	            fourHeader = "FOUR" + CricketFunctions
//	                            .Plural(inn.getTotalFours())
//	                            .toUpperCase();
//	            teamBadge  = inn.getBatting_team().getTeamBadge();
//	        }
//	    }
//
//	    String populateXml =
//	    	    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//	    	    "<payload xmlns=\"http://www.vizrt.com/types\" " +
//	    	    "model=\"http://10.100.2.1:8580/models/vdf/storage/shows/%7B25B3F0C6-1D65-4DFC-89C8-8ECFB75EFC1A%7D/mastertemplates/RunsAndBall\">" +
//
//	    	    "<field name=\"003-Header\">" +
//	    	    "<value>RISHABH</value>" +
//	    	    "</field>" +
//
//	    	    "<field name=\"100-HEADER\">" +
//	    	    "<value>" + fourHeader + "</value>" +
//	    	    "</field>" +
//
//	    	    "<field name=\"101-RUNS\">" +
//	    	    "<value>" + totalFours + "</value>" +
//	    	    "</field>" +
//
//	    	    "<field name=\"102-HEADER\">" +
//	    	    "<value>ANKIT</value>" +
//	    	    "</field>" +
//
//	    	    "<field name=\"103-BALLS\">" +
//	    	    "<value>" + totalSixes + "</value>" +
//	    	    "</field>" +
//
//	    	    "</payload>";
//
////	    String cueXml =
////	        "<mos>" +
////	        "  <cue>" +
////	        "    <channel>PROGRAM</channel>" +
////	        "    <page>" + pageName + "</page>" +
////	        "  </cue>" +
////	        "</mos>";
////
////	    String takeXml =
////	        "<mos>" +
////	        "  <take>" +
////	        "    <channel>PROGRAM</channel>" +
////	        "    <page>" + pageName + "</page>" +
////	        "  </take>" +
////	        "</mos>";
//	    ;
//
//	    try {
//	        sendMseXmlRequest(
//	            MSE_URL, populateXml,
//	            "MSE STEP 1 - BOUNDARY PAGE POPULATED"
//	        );
//	    } catch (Exception e) {
//	        System.err.println(
//	            "[VizMSE] Boundary MSE failed: "
//	            + e.getMessage()
//	        );
//	        e.printStackTrace();
//	    }
//	}
//
//	private void sendMseXmlRequest(
//	        String mseUrl,
//	        String xml,
//	        String step) throws Exception {
//
//	    java.net.URL url = new java.net.URL(mseUrl);
//
//	    java.net.HttpURLConnection conn =
//	            (java.net.HttpURLConnection) url.openConnection();
//
//	    conn.setRequestMethod("PUT");
//	    conn.setDoOutput(true);
//	    conn.setRequestProperty("Content-Type", "application/vnd.vizrt.payload+xml;type=element");
//	    conn.setConnectTimeout(3000);
//	    conn.setReadTimeout(3000);
//
//	    System.out.println("====================================");
//	    System.out.println(step);
//	    System.out.println("URL : " + mseUrl);
//	    System.out.println("XML : ");
//	    System.out.println(xml);
//	    System.out.println("====================================");
//
//	    try (java.io.OutputStream os = conn.getOutputStream()) {
//	        os.write(xml.getBytes("UTF-8"));
//	        os.flush();
//	    }
//
//	    int responseCode = conn.getResponseCode();
//
//	    System.out.println(step + " -> HTTP " + responseCode);
//
//	    java.io.InputStream is;
//
//	    if (responseCode >= 200 && responseCode < 300) {
//	        is = conn.getInputStream();
//	    } else {
//	        is = conn.getErrorStream();
//	    }
//
//	    if (is != null) {
//
//	        try (java.io.BufferedReader br =
//	                new java.io.BufferedReader(
//	                        new java.io.InputStreamReader(is))) {
//
//	            String line;
//
//	            System.out.println("----- RESPONSE -----");
//
//	            while ((line = br.readLine()) != null) {
//	                System.out.println(line);
//	            }
//
//	            System.out.println("--------------------");
//	        }
//	    }
//
//	    conn.disconnect();
//	}
	
	public void DoadWriteToTrio(PrintWriter print_writer,String sendCommand) {
		print_writer.println(sendCommand + return_key + line_feed);
	}
	
	public static String[] splitString(String str) {
	    int index = str.indexOf("\t");
	    List<String> parts = new ArrayList<>();
	    
	    if (index != -1) {
	        parts.add(str.substring(0, index));
	        String[] rest = str.substring(index + 1).split("\t");
	        for (String part : rest) {
	            if (!part.isEmpty()) {
	                parts.add(part.trim());
	            }
	        }
	    } else {
	        parts.add(str);
	    }
	    
	    return parts.toArray(new String[0]);
	}
	public String getBowlerRunsOverbyOver(int inning,List<Event> event, MatchAllData matchAllData) {
		
		int bowlerId = 0,runs = 0,wicket = 0;
		String name = "";
		boolean bowler_found = false;
		
		if ((matchAllData.getEventFile().getEvents() != null) && (matchAllData.getEventFile().getEvents().size() > 0)) {
			for(Event evnt: matchAllData.getEventFile().getEvents()) {
				if(evnt.getEventInningNumber() == inning) {
					if(evnt.getEventExtra() != null) {
						if(evnt.getEventExtra().equalsIgnoreCase("TAPE")) {
							bowlerId = evnt.getEventBowlerNo();
							bowler_found = true;
						}
					}
					if(bowler_found && evnt.getEventBowlerNo() == bowlerId) {
						switch(evnt.getEventType()) {
						case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
		            	case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
		            		runs += evnt.getEventRuns();
		                    break;
		            	case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
		            		runs += evnt.getEventRuns();
		                    break;

		            	case CricketUtil.LOG_WICKET:
		                    if (evnt.getEventRuns() > 0)
		                    {
		                    	runs += evnt.getEventRuns();
		                    }
		                    wicket += 1;
		                    break;

		            	case CricketUtil.LOG_ANY_BALL:
		            		runs += evnt.getEventRuns();
		                    if (evnt.getEventExtra() != null)
		                    {
		                    	runs += evnt.getEventExtraRuns();
		                    }
		                    if (evnt.getEventSubExtra() != null)
		                    {
		                    	runs += evnt.getEventSubExtraRuns();
		                    }
		                    break;										
						}
					}else if(evnt.getEventBowlerNo() != bowlerId && evnt.getEventBowlerNo() != 0) {
						bowler_found = false;
					}
				}
			}
		}
		
		for (BowlingCard boc : matchAllData.getMatch().getInning().get(inning - 1).getBowlingCard()) {
			if(boc.getPlayerId() == bowlerId) {
				name = boc.getPlayer().getTicker_name();
			}
		}
		
		return name + "," + runs + "," + wicket;
		
	}
	private void sendVizMSEBoundaryPage(String pageName, MatchAllData match, int inningNumber,String ipAddress,String showName) {

		 String[] urls = getShowIdAndURL(pageName,match,ipAddress,"RunsAndBall",showName);

		    String modelUrl = urls[0];
		    String putUrl   = urls[1];       

	    String totalFours = "0";
	    String totalSixes = "0";
	    String fourHeader = "";
	    
	    for (Inning inn : match.getMatch().getInning()) {
	        if (inn.getInningNumber() == inningNumber) {
	            totalFours = String.valueOf(inn.getTotalFours());
	            totalSixes = String.valueOf(inn.getTotalSixes());
	           fourHeader = "FOUR" + CricketFunctions.Plural(inn.getTotalFours()).toUpperCase();
	        }
	    }

	    String payloadXml =
	          "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
	        + "<payload xmlns=\"http://www.vizrt.com/types\"\n"
	        + "         model=\"" + modelUrl + "\">\n"
	        + "  <field name=\"003-Header\"><value>BOUNDARIES</value></field>\n"
	        + "  <field name=\"100-HEADER\"><value>" + fourHeader + "</value></field>\n"
	        + "  <field name=\"101-RUNS\"><value>"   + totalFours + "</value></field>\n"
	        + "  <field name=\"102-HEADER\"><value>SIXES</value></field>\n"
	        + "  <field name=\"103-BALLS\"><value>"  + totalSixes + "</value></field>\n"
	        + "</payload>";

	    putMethod(putUrl,payloadXml);
	}
	


//	private int msePutPage(String baseUrl, String showGuid, String pageNumber, String payloadXml)
//	        throws Exception {
//
//	    String url = baseUrl + "/element_collection/storage/shows/%7B"
//	               + showGuid + "%7D/elements/" + pageNumber + "/";
//
//	    System.out.println("[MSE] PUT -> " + url);
//
//	    HttpRequest request = HttpRequest.newBuilder()
//	            .uri(URI.create(url))
//	            .PUT(HttpRequest.BodyPublishers.ofString(payloadXml))
//	            .header("Content-Type", "application/vnd.vizrt.payload+xml;type=element")
//	            .timeout(Duration.ofSeconds(5))
//	            .build();
//
//	    HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
//	    System.out.println("[MSE] PUT response: " + response.body());
//	    return response.statusCode();
//	}
//
//	private int msePostPage(String baseUrl, String showGuid, String pageNumber, String payloadXml)
//	        throws Exception {
//
//	    String url = baseUrl + "/element_collection/storage/shows/%7B" + showGuid + "%7D/";
//
//	    System.out.println("[MSE] POST -> " + url);
//
//	    HttpRequest request = HttpRequest.newBuilder()
//	            .uri(URI.create(url))
//	            .POST(HttpRequest.BodyPublishers.ofString(payloadXml))
//	            .header("Content-Type", "application/vnd.vizrt.payload+xml;type=element")
//	            .header("Slug", pageNumber)
//	            .timeout(Duration.ofSeconds(5))
//	            .build();
//
//	    HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
//	    System.out.println("[MSE] POST response: " + response.body());
//	    return response.statusCode();
//	}

	private String buildBoundaryPayload(String modelUrl, String totalFours, String totalSixes, String fourHeader) {
	    return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
	        + "<payload xmlns=\"http://www.vizrt.com/types\"\n"
	        + "         model=\"" + modelUrl + "\">\n"
	        + "  <field name=\"003-Header\"><value>BOUNDARIES</value></field>\n"
	        + "  <field name=\"100-HEADER\"><value>" + escapeXml(fourHeader) + "</value></field>\n"
	        + "  <field name=\"101-RUNS\"><value>"   + escapeXml(totalFours) + "</value></field>\n"
	        + "  <field name=\"102-HEADER\"><value>SIXES</value></field>\n"
	        + "  <field name=\"103-BALLS\"><value>"  + escapeXml(totalSixes) + "</value></field>\n"
	        + "</payload>";
	}

	private String escapeXml(String text) {
	    if (text == null) return "";
	    return text.replace("&", "&amp;")
	               .replace("<", "&lt;")
	               .replace(">", "&gt;")
	               .replace("\"", "&quot;")
	               .replace("'", "&apos;");
	}
	private void sendVizMSEEquationPage(String pageName, MatchAllData match,String ipAddress,String showName) {

		
		 String[] urls = getShowIdAndURL(pageName,match,ipAddress,"RunsAndBall",showName);

		    String modelUrl = urls[0];
		    String putUrl   = urls[1];

		  // Get target data
	    String remainingRuns = String.valueOf(
	            CricketFunctions.GetTargetData(match).getRemaningRuns());
	    String remainingBalls = String.valueOf(
	            CricketFunctions.GetTargetData(match).getRemaningBall());

	    String payloadXml =
	          "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
	        + "<payload xmlns=\"http://www.vizrt.com/types\"\n"
	        + "         model=\"" + modelUrl + "\">\n"
	        + "  <field name=\"003-Header\"><value>TARGET</value></field>\n"
	        + "  <field name=\"100-HEADER\"><value>NEED</value></field>\n"
	        + "  <field name=\"101-RUNS\"><value>" + remainingRuns + "</value></field>\n"
	        + "  <field name=\"102-HEADER\"><value>FROM</value></field>\n"
	        + "  <field name=\"103-BALLS\"><value>" + remainingBalls + "</value></field>\n"
	        + "</payload>";

		    System.out.println("=== MSE EQUATION PUT URL ===");
		    System.out.println(putUrl);
		    System.out.println("=== PAYLOAD ===");
		    System.out.println(payloadXml);
		    System.out.println("===============");

	     putMethod(putUrl,payloadXml);
	}
	private String getShowGuid(String showName,String ipAddress) {

	    try {
	    	
	        HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create("http://"+ ipAddress  +":8580/directory/shows/"))
	                .GET()
	                .build();

	        HttpResponse<String> response =
	                HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

	        String xml = response.body();

	        DocumentBuilderFactory factory =
	                DocumentBuilderFactory.newInstance();

	        factory.setNamespaceAware(false);

	        DocumentBuilder builder =
	                factory.newDocumentBuilder();

	        Document document =
	                builder.parse(new InputSource(new StringReader(xml)));

	        NodeList entries = document.getElementsByTagName("entry");

	        for (int i = 0; i < entries.getLength(); i++) {

	            Element entry = (Element) entries.item(i);

	            String title =
	                    entry.getElementsByTagName("title")
	                            .item(0)
	                            .getTextContent();

	            if (showName.equalsIgnoreCase(title)) {

	                NodeList links =
	                        entry.getElementsByTagName("link");

	                for (int j = 0; j < links.getLength(); j++) {

	                    Element link = (Element) links.item(j);

	                    String href = link.getAttribute("href");

	                    if (href.contains("/show/")) {

	                        int start = href.indexOf("/show/") + 6;
	                        int end = href.lastIndexOf("/");

	                        String guid = href.substring(start, end);

	                        System.out.println(
	                                "Found Show GUID for "
	                                + showName + " : "
	                                + guid);

	                        return guid;
	                    }
	                }
	            }
	        }

	        throw new RuntimeException(
	                "Show not found : " + showName);

	    } catch (Exception e) {

	        e.printStackTrace();

	        throw new RuntimeException(
	                "Unable to fetch show GUID", e);
	    }
	}
	
	
	private void sendVizMSEMostWicketsPage(String pageName,MatchAllData match,List<Tournament> tournament_stats,CricketService cricketService,
	        String cat,String ipAddress,String showName) {
			
			String[] urls = getShowIdAndURL(pageName,match,ipAddress,"Leaderboard",showName);

		    String modelUrl = urls[0];
		    String putUrl   = urls[1];
			try {
			
			String payloadXml =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			+ "<payload xmlns=\"http://www.vizrt.com/types\"\n"
			+ "         model=\"" + modelUrl + "\">\n"
			+ "  <field name=\"004-SELECT_CAP\"><value>2</value></field>\n"
			+ "  <field name=\"003-HEADER\"><value>MOST WICKETS</value></field>\n";
			
			
			int max = Math.min(5, tournament_stats.size());
			
			for (int i = 0; i < max; i++) {
			
			Tournament t = tournament_stats.get(i);
			int row = i + 1;
			
			Team team = cricketService.getTeams()
			.stream()
			.filter(tm -> tm.getTeamId() == t.getPlayer().getTeamId())
			.findFirst()
			.orElse(null);
			
			String teamBadge = (team != null) ? team.getTeamBadge() : "";
			String playerName = t.getPlayer().getTicker_name();
			String runs = String.valueOf(t.getWickets());
			
			String imagePath = "";
			
			if (team != null) {
			imagePath =
			"C:\\\\Images\\\\T20_MUMBAI\\\\PHOTOS\\"
			+ cat + "\\\\BIG_1024\\\\"
			+ team.getTeamName4() + "\\\\"
			+ t.getPlayer().getPhoto()
			+ CricketUtil.PNG_EXTENSION;
			}
			
			payloadXml +=
			"  <field name=\"" + row + "00-TEAMNAME\"><value>"
			    + teamBadge + "</value></field>\n"
			+ "  <field name=\"" + row + "02-NAME\"><value>"
			    + playerName + "</value></field>\n"
			+ "  <field name=\"" + row + "05-STAT\"><value>"
			    + runs + "</value></field>\n"
			 + "  <field name=\"" + row + "04-STAT\"><value>"
			    + (i+1) + "</value></field>\n"
			+ "  <field name=\"" + row + "01-PLAYER-IMAGE\"><value>"
			    + imagePath + "</value></field>\n";
			}
			
			payloadXml += "</payload>";
			
			System.out.println("=== MSE MOST RUNS PUT URL ===");
			System.out.println(putUrl);
			System.out.println("=== PAYLOAD ===");
			System.out.println(payloadXml);
			
			putMethod(putUrl,payloadXml);
			
			} catch (Exception e) {
			System.err.println("[MSE] ERROR in sendVizMSEMostRunsPage: " + e.getMessage());
			e.printStackTrace();
			}
	}
			private void sendVizMSELastXBallsPage(String pageName,
		            MatchAllData match,
		            int ballNo,
		            String ipAddress,
		            String showName) {
		
		String[] urls = getShowIdAndURL(
		pageName,
		match,
		ipAddress,
		"RunsAndBall",
		showName);
		
		String modelUrl = urls[0];
		String putUrl   = urls[1];
		
		List<String> thisDataStr = new ArrayList<>();
		
		thisDataStr.add(
		CricketFunctions.getlastthirtyballsdata(
		match,
		"-",
		match.getEventFile().getEvents(),
		ballNo));
		
		String[] data =
		thisDataStr.get(thisDataStr.size() - 1).split("-");
		
		int runs = Integer.parseInt(data[0]);
		int wickets = Integer.parseInt(data[1]);
		
		String runHeader =
		"RUN" + CricketFunctions.Plural(runs).toUpperCase();
		
		String wicketHeader =
		"WICKET" + CricketFunctions.Plural(wickets).toUpperCase();
		
		String payloadXml =
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
		+ "<payload xmlns=\"http://www.vizrt.com/types\"\n"
		+ "         model=\"" + modelUrl + "\">\n"
		
		+ "  <field name=\"003-Header\"><value>LAST "
		+ ballNo
		+ " BALLS</value></field>\n"
		
		+ "  <field name=\"100-HEADER\"><value>"
		+ runHeader
		+ "</value></field>\n"
		
		+ "  <field name=\"101-RUNS\"><value>"
		+ runs
		+ "</value></field>\n"
		
		+ "  <field name=\"102-HEADER\"><value>"
		+ wicketHeader
		+ "</value></field>\n"
		
		+ "  <field name=\"103-BALLS\"><value>"
		+ wickets
		+ "</value></field>\n"
		
		+ "</payload>";
		
		putMethod(putUrl, payloadXml);
		}
	
	
			private void sendVizMSEVictoryPage(String pageName,
                    MatchAllData match,
                    String ipAddress,
                    String showName) {
			
			String[] urls = getShowIdAndURL(
			pageName,
			match,
			ipAddress,
			"Victory",
			showName);
			
			String modelUrl = urls[0];
			String putUrl = urls[1];
			
			String targetOrResult =
			CricketFunctions.GenerateMatchSummaryStatus(
			     2,
			     match,
			     CricketUtil.FULL,
			     "",
			     "T20_MUMBAI",
			     true)
			.getTargetOrResult();
			
			String result =
			"WIN "
			+ targetOrResult
			     .split("win")[1]
			     .replace("remaining", "to spare")
			     .toUpperCase();
			
			String payloadXml =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			+ "<payload xmlns=\"http://www.vizrt.com/types\"\n"
			+ "         model=\"" + modelUrl + "\">\n"
			
			+ "  <field name=\"003-Header\">"
			+ "<value>" + result + "</value></field>\n"
			
			+ "  <field name=\"004-Header\">"
			+ "<value></value></field>\n"
			
			+ "</payload>";
			
			putMethod(putUrl, payloadXml);
			}
			
						
	private void sendVizMSEMostRunsPage(String pageName,MatchAllData match,List<Tournament> tournament_stats,CricketService cricketService,
        String cat,String ipAddress,String showName) {
		
		String[] urls = getShowIdAndURL(pageName,match,ipAddress,"Leaderboard",showName);

	    String modelUrl = urls[0];
	    String putUrl   = urls[1];
		try {
		
		String payloadXml =
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
		+ "<payload xmlns=\"http://www.vizrt.com/types\"\n"
		+ "         model=\"" + modelUrl + "\">\n"
		+ "  <field name=\"004-SELECT_CAP\"><value>1</value></field>\n"
		+ "  <field name=\"003-HEADER\"><value>MOST RUNS</value></field>\n";
		
		
		int max = Math.min(5, tournament_stats.size());
		
		for (int i = 0; i < max; i++) {
		
		Tournament t = tournament_stats.get(i);
		int row = i + 1;
		
		Team team = cricketService.getTeams()
		.stream()
		.filter(tm -> tm.getTeamId() == t.getPlayer().getTeamId())
		.findFirst()
		.orElse(null);
		
		String teamBadge = (team != null) ? team.getTeamBadge() : "";
		String playerName = t.getPlayer().getTicker_name();
		String runs = String.valueOf(t.getRuns());
		
		String imagePath = "";
		
		if (team != null) {
		imagePath =
		"C:\\\\Images\\\\T20_MUMBAI\\\\PHOTOS\\"
		+ cat + "\\\\BIG_1024\\\\"
		+ team.getTeamName4() + "\\\\"
		+ t.getPlayer().getPhoto()
		+ CricketUtil.PNG_EXTENSION;
		}
		
		payloadXml +=
		"  <field name=\"" + row + "00-TEAMNAME\"><value>"
		    + teamBadge + "</value></field>\n"
		+ "  <field name=\"" + row + "02-NAME\"><value>"
		    + playerName + "</value></field>\n"
		+ "  <field name=\"" + row + "05-STAT\"><value>"
		    + runs + "</value></field>\n"
		 + "  <field name=\"" + row + "04-STAT\"><value>"
		    + (i+1) + "</value></field>\n"
		+ "  <field name=\"" + row + "01-PLAYER-IMAGE\"><value>"
		    + imagePath + "</value></field>\n";
		}
		
		payloadXml += "</payload>";
		
		System.out.println("=== MSE MOST RUNS PUT URL ===");
		System.out.println(putUrl);
		System.out.println("=== PAYLOAD ===");
		System.out.println(payloadXml);
		
		putMethod(putUrl,payloadXml);
		
		} catch (Exception e) {
		System.err.println("[MSE] ERROR in sendVizMSEMostRunsPage: " + e.getMessage());
		e.printStackTrace();
		}
}
	
	private String[] getShowIdAndURL(String pageName, MatchAllData match,String iP,String templateName,String showName){
		
		String SHOW_GUID = getShowGuid(showName,iP);
	    String BASE_URL  = "http://" + iP + ":8580";
	    String TEMPLATE  = templateName;
	    String PAGE_NO   = pageName;
	    String modelUrl = BASE_URL + "/models/vdf/storage/shows/"
	                    + SHOW_GUID + "/mastertemplates/" + TEMPLATE;

	    String putUrl = BASE_URL + "/element_collection/storage/shows/"
	                  + SHOW_GUID + "/elements/" + PAGE_NO + "/";
	    
	   
	    return new String[]{modelUrl, putUrl};

	}
	private void putMethod(String putUrl,String payloadXml) {
		try {
	        HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(putUrl))
	                .PUT(HttpRequest.BodyPublishers.ofString(payloadXml))
	                .header("Content-Type",
	                        "application/vnd.vizrt.payload+xml;type=element")
	                .timeout(Duration.ofSeconds(5))
	                .build();

	        HttpResponse<String> response = HTTP_CLIENT.send(
	                request, HttpResponse.BodyHandlers.ofString());

	        System.out.println("[MSE] HTTP " + response.statusCode());
	        System.out.println("[MSE] Response: " + response.body());

	        if (response.statusCode() == 200) {
	            System.out.println("page update");
	        } else {
	            System.out.println(" error " + response.statusCode());
	        }

	    } catch (Exception e) {
	        System.err.println(" error: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
	
}