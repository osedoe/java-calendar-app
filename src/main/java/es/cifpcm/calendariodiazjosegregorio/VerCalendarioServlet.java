/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.cifpcm.calendariodiazjosegregorio;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Jose Gregorio Diaz Gonzalez
 *
 * ////
 * DISCLAIMER: Do not attemp to run this application on Internet Explorer, 
 * since the style used in the calendar (CSS Grid) is not supported.
 * ////
 */
public class VerCalendarioServlet extends HttpServlet {
    
    // Initialize instances of calendar
    Calendar cal;
    Calendar calAnterior;
    Calendar hoy;
    Calendar calPosterior;
    
    // Initialize lists for each month that we are going to display
    List<String> diasDelMes;
    List<String> diasPrevios;
    List<String> diasPosteriores;
    
    // Other variables
    int ultimoDiaDelMes; // Used to get the length of the month
    int numeroMes = 0; // To select each month
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException i f an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
     
        response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        
        // Get the parameter for the chosen month through the GET action
        String mes = request.getParameter("mes").toLowerCase();
        
        try {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Calendario Jose</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<link rel=\"stylesheet\" href=\"css/style.css\">");
            out.println("<div class=\"container\">");
            out.println("<h1>Calendario</h1>");
            
            switch (mes) {
                // Undefined (mes sin definir) case
                case "":
                    out.println("<p class=\"text\">&lt;Mes sin definir</p>");
                    break;
                    
                case "enero":
                    numeroMes = 1;
                    break;

                case "febrero":
                    numeroMes = 2;
                    break;

                case "marzo":
                    numeroMes = 3;
                    break;

                case "abril":
                    numeroMes = 4;
                    break;

                case "mayo":
                    numeroMes = 5;
                    break;

                case "junio":
                    numeroMes = 6;
                    break;

                case "julio":
                    numeroMes = 7;
                    break;

                case "agosto":
                    numeroMes = 8;
                    break;

                case "septiembre":
                    numeroMes = 9;
                    break;

                case "octubre":
                    numeroMes = 10;
                    break;

                case "noviembre":
                    numeroMes = 11;
                    break;

                case "diciembre":
                    numeroMes = 12;
                    break;
                    
                default:
                    System.out.println("El valor introducido no es un mes válido");
                    
            }
            
            // If month IS defined (all calendar's html)
            if (!"".equals(mes)) {
                // Header
                out.println("<div class=\"month\">");
                    out.println("<ul>");
                        out.println("<li>" + request.getParameter("mes") + " de 2018</li>");
                    out.println("</ul>");
                out.println("</div>");

                // Days of the week
                out.println("<ul class=\"weekdays\">");
                    out.println("<li class=\"monday\">L</li>");
                    out.println("<li class=\"tuesday\">M</li>");
                    out.println("<li class=\"wednesday\">X</li>");
                    out.println("<li class=\"thursday\">J</li>");
                    out.println("<li class=\"friday\">V</li>");
                    out.println("<li class=\"saturday\">S</li>");
                    out.println("<li class=\"sunday\">D</li>");
                out.println("</ul>");
            
                // Find out the length of the month
                configurarMes(numeroMes);
                
                out.println("<ul class=\"days\">");
                    
                // Iterate through the days of the previous month
                rellenarDiasPrevios(adivinarPrimerDia(numeroMes));
                for(int i = 0; i < diasPrevios.size(); i++) {
                    out.println(diasPrevios.get(i));
                }
                
                // Iterate through the days of the selected month
                for (int i = 0; i < diasDelMes.size(); i++) {
                    out.println(diasDelMes.get(i));
                }
                
                // Iterate through the days of the next month
                rellenarDiasPosteriores();
                for (int i = 0; i < diasPosteriores.size(); i++) {
                    out.println(diasPosteriores.get(i));
                }
                
                out.println("</ul>");           
                out.println("</div>");

            } // End IF
            
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }
    
    // CUSTOM METHODS
    
    /**
     * Method that finds the weekday of the last day of the month and populate 
     * the next month based on the gaps in the calendar
     */
    public void rellenarDiasPosteriores() {
        
        diasPosteriores = new ArrayList();
        
        calPosterior = new GregorianCalendar();
        calPosterior.set(Calendar.MONTH, numeroMes-1);
        
        int ultimoDiaMes = calPosterior.getActualMaximum(Calendar.DAY_OF_MONTH);
        calPosterior.set(Calendar.DAY_OF_MONTH, ultimoDiaMes);
        int diaSemanaUltimo = calPosterior.get(Calendar.DAY_OF_WEEK)-1;
        
        int dia = 0;
        
        switch (diaSemanaUltimo) {
            case 1:
                dia = 6;
                break;
                
            case 2:
                dia = 5;
                break;
                
            case 3:
                dia = 4;
                break;
                
            case 4:
                dia = 3;
                break;
                
            case 5:
                dia = 2;
                break;
                
            case 6:
                dia = 1;
                break;           
                
        }
        for (int i = 1; i < dia; i++) {
            diasPosteriores.add("<li class=\"pos\">" + i + "</li>");
        }
        
    }
    
    /**
     * Method that fills the gaps in the calendar with the previous days of the month
     * @param mesEmpiezaEn 
     */
    public void rellenarDiasPrevios(int mesEmpiezaEn) {
        
        diasPrevios = new ArrayList();
        
        calAnterior = new GregorianCalendar();
        calAnterior.set(Calendar.MONTH, numeroMes-2);
        int ultimoDiaMesAnterior = calAnterior.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        switch(mesEmpiezaEn) {    
            
            case 2:
                diasPrevios.add("<li class=\"prev\">" + ultimoDiaMesAnterior + "</li>");
                break;
                
            case 3:
                ultimoDiaMesAnterior -= 1;
                for (int i = 0; i < 2; i++) {
                   diasPrevios.add("<li class=\"prev\">" + ultimoDiaMesAnterior + "</li>");
                   ultimoDiaMesAnterior++;
                }
                break;
                
            case 4:
                ultimoDiaMesAnterior -= 2;
                for (int i = 0; i < 3; i++) {
                    diasPrevios.add("<li class=\"prev\">" + ultimoDiaMesAnterior + "</li>");
                    ultimoDiaMesAnterior++;
                }
                break;
                
            case 5:
                ultimoDiaMesAnterior -= 3;
                for (int i = 0; i < 4; i++) {
                    diasPrevios.add("<li class=\"prev\">" + ultimoDiaMesAnterior + "</li>");
                    ultimoDiaMesAnterior++;
                }
                break;
                
            case 6:
                ultimoDiaMesAnterior -= 4;
                for (int i = 0; i < 5; i++) {
                    diasPrevios.add("<li class=\"prev\">" + ultimoDiaMesAnterior + "</li>");
                    ultimoDiaMesAnterior++;
                }
                break;
                
            case 7:
                ultimoDiaMesAnterior -= 5;
                for (int i = 0; i < 6; i++) {
                    diasPrevios.add("<li class=\"prev\">" + ultimoDiaMesAnterior + "</li>");
                    ultimoDiaMesAnterior++;
                }
                break;
                
        }
    }
    
    // Find today to pass it inside the "rellenarMes()" method to check the day
    public int diaActual() {
        hoy = new GregorianCalendar();
        int resultado = hoy.get(Calendar.DATE);
        return resultado;
    }
    
    // Fill the selected month
    public void rellenarMes(int dias) {
        
        diasDelMes = new ArrayList();
        
        for (int i = 1; i <= dias; i++) {
            if (i == diaActual()) {
                diasDelMes.add("<li class=\"hoy\">" + i + "</li>");
            }
            diasDelMes.add("<li>" + i + "</li>");
        }
    }
    
    // Used to populate the days of the month before, by finding the day of the week
    public int adivinarPrimerDia(int numeroMes) {
        calAnterior = new GregorianCalendar();
        calAnterior.set(Calendar.MONTH, numeroMes-1);
        calAnterior.set(Calendar.DAY_OF_MONTH, 0);
        int firstDay = calAnterior.get(Calendar.DAY_OF_WEEK);
        return firstDay;
    }
    
    // Find the last day of the month to fill it with the days
    public void configurarMes(int numeroMes) {
        cal = new GregorianCalendar();
        cal.set(Calendar.MONTH, numeroMes-1);
        
        ultimoDiaDelMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        rellenarMes(ultimoDiaDelMes);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
