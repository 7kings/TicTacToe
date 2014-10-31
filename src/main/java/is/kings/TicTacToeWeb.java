package is.kings;

import spark.*;
import static spark.Spark.*;
import spark.servlet.SparkApplication;

/**
 * A class that implements SparkApplication and makes TicTacToe a web app.
 */
public class TicTacToeWeb implements SparkApplication{
	private Grid grid = new Grid();

	public static void main(String[] args){
		staticFileLocation("/public");
		SparkApplication TTTWeb = new TicTacToeWeb();
		String port = System.getenv("PORT");
		if (port != null) {
           		 setPort(Integer.valueOf(port));
       		 }
		TTTWeb.init();
	}

	public void init(){
		final HumanPlayer hPlayer = new HumanPlayer();
		final ComputerPlayer cPlayer = new ComputerPlayer();
		get(new Route("/newgame"){
			@Override
                        public Object handle(Request req, Response res){
				grid = new Grid();
				res.redirect("/");
				return res;
			}
		});
		post(new Route("/fight"){
			@Override
			public Object handle(Request req, Response res){
				if(grid.gameConclusion()){
					StringBuilder html = new StringBuilder();
                                        html.append("<pre>").append(grid.printGrid()).append("</pre>");
                                        html.append("<pre>").append("GAME OVER").append("</pre>");
                                        String gridOut = html.toString();
                                        return gridOut;
				}

				String inString = String.valueOf(req.queryParams("move"));
				if(!hPlayer.checkInput(inString)){
					StringBuilder html = new StringBuilder();
                                        html.append("<pre>").append(grid.printGrid()).append("</pre>");
                                        html.append("<pre>").append("Invalid move , try again").append("</pre>");
                                        String gridOut = html.toString();
					return gridOut;

				}
				Integer number = Integer.valueOf(req.queryParams("move"));
				if(!hPlayer.playerMove(grid, number)){
					StringBuilder html = new StringBuilder();
                                	html.append("<pre>").append(grid.printGrid()).append("</pre>");
					html.append("<pre>").append("Invalid move, try again").append("</pre>");
                                	String gridOut = html.toString();
                                	return gridOut;
				}
				if(grid.gameConclusion()){
					StringBuilder html = new StringBuilder();
                                        html.append("<pre>").append(grid.printGrid()).append("</pre>");
                                        html.append("<pre>").append("YOU WIN!").append("</pre>");
                                        String gridOut = html.toString();
                                        return gridOut;
				}
				while(!cPlayer.computerMove(grid)){}
				if(grid.gameConclusion()){
                                        StringBuilder html = new StringBuilder();
                                        html.append("<pre>").append(grid.printGrid()).append("</pre>");
                                        html.append("<pre>").append("COMPUTER WINS!").append("</pre>");
                                        String gridOut = html.toString();
                                        return gridOut;
                                }
				StringBuilder html = new StringBuilder();
				html.append("<pre>").append(grid.printGrid()).append("</pre>");
				String gridOut = html.toString();
				return gridOut;
			}
		});
	}
}