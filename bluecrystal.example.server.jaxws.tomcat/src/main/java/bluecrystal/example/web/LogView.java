package bluecrystal.example.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import bluecrystal.example.web.domain.LogStructure;
import bluecrystal.example.web.domain.SignedEnvelope;
import bluecrystal.example.web.util.LogAnalizer;

/**
 * Servlet implementation class LogView
 */
@WebServlet("/LogView")
public class LogView extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LogAnalizer logAnalizer;
	private String logPath;
	final static Logger logger = LoggerFactory.getLogger(LogView.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogView() {
        super();
        logAnalizer = new LogAnalizer();
        logPath = logAnalizer.getLogPath();
        System.out.println("** logPath: "+logPath);
        logger.debug("Registro de referência");
        logger.error("Registro de referência");
        logger.info("Registro de referência");
        logger.trace("Registro de referência");
        logger.warn("Registro de referência");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		printPage("", response);
	}

	private void printPage(String fileName, HttpServletResponse response) {
		try {
			Gson gson = new Gson();
			String hmtlPath = (fileName == null || fileName.isEmpty())?logPath:fileName;
			String content = gson.toJson(new LogStructure(hmtlPath, logAnalizer.countLines(), logAnalizer.getFullContent()));
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	private void printPage2(String fileName, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html><body>");
		out.println("Log Path: ("+logAnalizer.countLines()+") "+"<br/>");
		out.println("<form method=\"POST\">");
		String hmtlPath = (fileName == null || fileName.isEmpty())?logPath:fileName;
		out.println("Arquivo: <input type=\"text\" name=\"arquivo\" size=\"100\"value=\""+logPath+"\"></input><br/>");
		out.println("<input type=\"submit\" value=\"Submit\">");
		out.println("</form>");
		out.println("**************************************************<br/>");
		out.println(logAnalizer.getFullContent());
		
		out.println("**************************************************<br/>");
		out.println("</body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileName = request.getParameter("arquivo");
		logger.debug("POST arquivo:  "+fileName);
		printPage(fileName, response);
	}

}
