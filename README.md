# easIE
easy Information Extraction: is an easy-to-use information extraction framework that extracts data about companies from heterogeneous Web sources in a semi-automatic manner. It allows admin users to extract data about companies from heterogeneous Web sources in a semi-automatic manner by only defining a configuration file. The framework is quickly and simply generating Web Information Extractors and Wrappers. easIE offers a set of wrappers for obtaining content from Static and Dynamic HTML pages by pointing to the html elements using css Selectors.

**Note: [Here](http://easie.iti.gr) you can find the web support of easIE**
<h2><u>Getting started</u></h2>

Each extractor extends AbstractHTMLExtractor and implements the `extractFields(List<ScrapableField> fields)` and `extractTable(String table_selector, List<ScrapableField> fields)` methods. There are four objects that extend AbstractHTMLExtractor:

1. `StaticHTMLExtractor` is responsible for extracting content from static HTML pages:

          StaticHTMLExtractor extractor = new StaticHTMLExtractor(base_url, relative_url);
          extractor.extractFields(fields);

2. `DynamicHTMLExtractor` is responsible for executing a number of events to a dynamic HTML page and extracting the defined contents: 

          DynamicHTMLExtractor extractor = new DynamicHTMLWrapper(base_url, relative_url, chrome_driver_path);
          extractor.browser_emulator.clickEvent(css_selector);
          extractor.extractFields(fields);

3. `GroupHTMLExtractor` is responsible for extracting content from a group of static HTML pages with similar structure:

          GroupHTMLExtractor extractor = new GroupHTMLExtractor(group_of_pages);
          extractor.extractFields(fields);
          
4. `PaginationIterator` is responsible for extracting data that are distributed in different pages:

          PaginationIterator extractor = new PaginationIterator(base_url, relative_url, next_page_selector);
          extractor.extractFields(fields);
          
## Requirements:
- Java17-jdk or Java1.8-jdk
- [chromedriver](https://chromedriver.chromium.org/downloads)  and Chrome Web broswer according to your system [For Dynamic Extraction].
- [ConfigurationSchema.json](https://github.com/tassosblackg/easIE/blob/soClose_patch/ConfigurationSchema.json)
- A website to crawl from, json schema [check example](https://github.com/tassosblackg/easIE/blob/soClose_patch/configuration_files/religiousgreece_example_group_url2.json) using css selectors.

## How To Use:
- 1 arg execution:

          >$ java -jar easIE.jar website2crawl.json
          
          *easIE.jar, ConfigurationSchema.json and chromedriver MUST be in the same folder.*

- 2 args execution:

          >$ java -jar easIE.jar website2crawl.json path2chromedriver
          
          *easIE.jar, ConfigurationSchema.json MUST be in the same folder.
          
## Example:
1.| Download or Get this example json [religiousgreece_example_group_url2.json](https://github.com/tassosblackg/easIE/blob/soClose_patch/configuration_files/religiousgreece_example_group_url2.json) 

2.| Edit *line 71*, changing to your path, where to store the results.

3.| Get the [*easIE.jar*](https://github.com/tassosblackg/easIE/releases), [*ConfigSchema.json*](https://github.com/tassosblackg/easIE/blob/soClose_patch/ConfigurationSchema.json), and the appropriate [*chromedriver*](https://chromedriver.chromium.org/downloads) (according to your Chrome Browser version).


4.| Execution:
          
          $> java -jar easIE.jar religiousgreece_example_group_url2.json 
          
           
 **NOTICE:**
 > easIE.jar, ConfigSchema.json, religiousgreece_example_group_url2.json and chromedriver are on the same directory.
 
------------------------------------------------------------------------------------------------------------------------------

## More Info:

> Project developed using Intellij IDE and Maven project manager.
