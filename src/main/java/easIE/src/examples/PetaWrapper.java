package easIE.src.examples;

import easIE.src.Company;
import easIE.src.CompanySearcher;
import easIE.src.Field;
import easIE.src.FieldType;
import easIE.src.MongoUtils;
import easIE.src.OutputFormatter.CompanySnippet;
import easIE.src.Wrappers.DynamicHTMLWrapper;
import easIE.src.Wrappers.StaticHTMLWrapper;
import easIE.src.SelectorType;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author vasgat
 */
public class PetaWrapper {
   public DynamicHTMLWrapper wrapper;
   private ArrayList<Field> tableFields;
   private ArrayList<Field> pageFields;
   
   public PetaWrapper() throws URISyntaxException, IOException, InterruptedException{
      tableFields = new ArrayList<Field>();
      pageFields = new ArrayList<Field>();
      Field field1 = new Field(
              "Company Name",
              "td:nth-child(2)", 
              SelectorType.rawtext, 
              SelectorType.CSS,
              FieldType.text,
              FieldType.text
      );
      Field field2 = new Field(
              "Company Link",
              "td:nth-child(2) > a", 
              SelectorType.rawtext, 
              SelectorType.CSS,
              FieldType.text,
              FieldType.link
      );      
      Field field3 = new Field(
              "Peta Mall Partner",
              "td:nth-child(4)", 
              SelectorType.rawtext, 
              SelectorType.CSS,
              FieldType.text,
              FieldType.text
      );      
      Field field4 = new Field(
              "Vegan Company",
              "td:nth-child(3)", 
              SelectorType.rawtext, 
              SelectorType.CSS,
              FieldType.text,
              FieldType.text
      );       
      Field field5 = new Field(
              "Features PETA Logo",
              "td:nth-child(5)", 
              SelectorType.rawtext, 
              SelectorType.CSS,
              FieldType.text,
              FieldType.text
      ); 
      
      Field field6 = new Field(
              "Does Tests On Animals",
              "true", 
              SelectorType.rawtext, 
              SelectorType.rawtext,
              FieldType.text,
              FieldType.text
      );       
      tableFields.add(field1);
      tableFields.add(field2);
      tableFields.add(field3);
      tableFields.add(field4);
      tableFields.add(field5);
      tableFields.add(field6);
      
      Field pfield1 = new Field(
              "Country",
              "#ctl00_ContentPlaceHolder1_l_Country",
              SelectorType.rawtext,
              SelectorType.CSS,
              FieldType.text,
              FieldType.text
      );
      
      Field pfield2 = new Field(
              "Company Link",
              "#ctl00_ContentPlaceHolder1_l_Website",
              SelectorType.rawtext,
              SelectorType.CSS,
              FieldType.text,
              FieldType.text
      );
      pageFields.add(pfield1);
      pageFields.add(pfield2);
      
      wrapper = new DynamicHTMLWrapper(
              "http://features.peta.org/cruelty-free-company-search/cruelty_free_companies_search.aspx?Donottest=-1&Product=0&Dotest=8&Regchange=-1&Country=-1&Keyword="
      );
      wrapper.clickEvent(
              "#ctl00_ContentPlaceHolder1_ddlResultCount > option:nth-child(4)"
      );
   }
   
   public void getCompaniesSnippets() throws URISyntaxException, IOException{
      ArrayList<HashMap<String, Object>> result = 
              wrapper.extractTable(
                      "#ctl00_ContentPlaceHolder1_gv_CompanySearch > tbody:nth-child(1) > tr", 
                      tableFields
              );
      
      MongoUtils mongo = new MongoUtils();
            CompanySearcher searcher = new CompanySearcher(mongo, "Wikirate2", "Companies");
      for (int i=0; i<result.size(); i++){         
         HashMap temp_Comp = result.get(i);
         StaticHTMLWrapper StaticWrapper = new StaticHTMLWrapper((String) temp_Comp.get("Company Link"));
         temp_Comp.putAll(StaticWrapper.extractFields(pageFields).get(0));
         System.out.println(temp_Comp);
         Company company;
         if (!temp_Comp.get("Company Link").equals("")&&
                           temp_Comp.get("Company Link")!=null &&
                               !temp_Comp.get("Company Link").equals("null")){
            company = new Company(
                    (String)temp_Comp.get("Company Name"), 
                    mongo, 
                    (String)temp_Comp.get("Company Link"), 
                    "Wikirate2",
                    "Companies",
                    searcher
            );
         }
         else{
            company = new Company(
                    (String)temp_Comp.get("Company Name"), 
                    mongo, 
                    "Wikirate2",
                    "Companies",
                    searcher
            );
         }
         if(!temp_Comp.get("Country").equals("")){
            company.insertInfo("Country", (String) temp_Comp.get("Country"));
         }
         temp_Comp.remove("Country");
         temp_Comp.remove("Company Name");
         temp_Comp.remove("Company Link");
         String source = (String) temp_Comp.get("source");
         int citeyear = (int) temp_Comp.get("citeyear");
         temp_Comp.remove("source");
         temp_Comp.remove("citeyear");
         Iterator compIt = temp_Comp.entrySet().iterator();
         while (compIt.hasNext()){
            Map.Entry entry = (Map.Entry) compIt.next();
            boolean snipValue = false;
            if (!entry.getValue().equals("")){
               snipValue = true;
            }
            CompanySnippet snippet = new CompanySnippet(
                 (String) entry.getKey(),
                 snipValue,
                 source,
                 null, 
                 citeyear,
                 company.getId(),
                 null,
                 "Peta",
                 null
            );
            snippet.store("Wikirate2", "Snippets", mongo);
         }
      }
      mongo.closeConnection();
      wrapper.quit();     
   }
   
   public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException{
      PetaWrapper peta = new PetaWrapper();
      peta.getCompaniesSnippets();
      peta.wrapper.quit();
   }
}