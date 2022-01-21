/*
 * Copyright 2016 vasgat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package certh.iti.mklab.easie;

import certh.iti.mklab.easie.configuration.Configuration;
import certh.iti.mklab.easie.configuration.ConfigurationReader;
import certh.iti.mklab.easie.exception.IllegalSchemaException;
import certh.iti.mklab.easie.exception.PaginationException;
import certh.iti.mklab.easie.exception.RelativeURLException;
import certh.iti.mklab.easie.executor.WrapperExecutor;
import certh.iti.mklab.easie.executor.handlers.DataHandler;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.json.JSONArray;
import org.jsoup.select.Selector.SelectorParseException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * @author vasgat
 */
public class Main {
    private void run_executor(WrapperExecutor executor, Configuration config) throws IOException {
        ArrayList entities = (ArrayList) executor.getCompanyInfo();
        ArrayList metrics = executor.getExtractedMetrics();

        DataHandler dh = new DataHandler(entities, metrics, config.entity_name);

        if (config.store != null) {
            dh.store(config.store, config.source_name);
            System.out.println("EXTRACTION TASK COMPLETED");
        } else {
            JSONArray array = new JSONArray(dh.exportJson());
            System.out.println(array.toString(4));
        }
    }

    public static void main(String[] args) throws URISyntaxException, NoSuchAlgorithmException {
        Main easie = new Main();
        if (args.length == 1) {
            try {
                ConfigurationReader reader = new ConfigurationReader(args[0], "." + System.getProperty("file.separator"));
                Configuration config = reader.getConfiguration();

                // set path according OS uses '\' or '/'
                String chrome_path = "." + System.getProperty("file.separator");


                if (System.getProperty("file.separator").equals("\\")) {

                    System.out.println("Windows");
                    chrome_path = chrome_path + "chromedriver.exe";
                } else {
                    chrome_path = chrome_path + "chromedriver";
                }

                WrapperExecutor executor = new WrapperExecutor(config, chrome_path);
                easie.run_executor(executor,config);

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (ProcessingException ex) {
                System.out.println(ex.getMessage());
            } catch (IllegalSchemaException ex) {
                System.out.println(ex.getMessage());
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            } catch (PaginationException ex) {
                System.out.println(ex.getMessage());
            } catch (RelativeURLException ex) {
                System.out.println(ex.getMessage());
            } catch (SelectorParseException ex) {
                System.out.println(ex.getMessage());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        }
        // if passing the chromedriver path as an extra argument
        else if (args.length == 2) {
            try {
                System.out.println("args: 1|"+args[0]+" args: 2|"+args[1]);
                ConfigurationReader reader = new ConfigurationReader(args[0], "." + System.getProperty("file.separator"));
                Configuration config = reader.getConfiguration();

                WrapperExecutor executor = new WrapperExecutor(config, args[1]);
                easie.run_executor(executor,config);

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (ProcessingException ex) {
                System.out.println(ex.getMessage());
            } catch (IllegalSchemaException ex) {
                System.out.println(ex.getMessage());
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            } catch (PaginationException ex) {
                System.out.println(ex.getMessage());
            } catch (RelativeURLException ex) {
                System.out.println(ex.getMessage());
            } catch (SelectorParseException ex) {
                System.out.println(ex.getMessage());
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        } else if (args.length == 0) {
            System.out.println("You need to provide the configuration file filepath!");
        } else {
            System.out.println("Maximum two vars can be defined...");
        }
    }

}
