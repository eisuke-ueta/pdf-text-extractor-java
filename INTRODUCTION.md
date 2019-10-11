# PDF Item Mapper

## Overview
`pdf-item-mapper` is a library that provides an easy way to extract text information from pdf and convert to structured data.

## Getting Started
### Build Java Code
Move to project directory and run the following command.
```bash
$ mvn compile
$ mvn package
```
After `mvn package` is successfully completed, `pdf-item-mapper-0.0.1-SNAPSHOT.jar` is created under target directory.

### Prepare Configuration and PDF File
Get `sample.json` and `sample.pdf` from Google Drive and place in your local.

### Run Jar
```bash
$ java -jar target/pdf-item-mapper-0.0.1-SNAPSHOT.jar PATH_TO_YOUR_SAMPLE_PDF PATH_TO_YOUR_SAMPLE_JSON
```

### Debugging
Add `true` to command line arguments. All text information of PDF file is output to console.
```bash
$ java -jar target/pdf-item-mapper-0.0.1-SNAPSHOT.jar PATH_TO_YOUR_SAMPLE_PDF PATH_TO_YOUR_SAMPLE_JSON true
```

## How To Configure Item Mapping Setting
There are two parts of item mapping settings:
- Header Spec: Specification for header item mapping
- Detail Spec: Specification for detail item mapping

Header spec is suitable for a single item mapping (Ex: Invoice No, Submite Date ...). Detail spec is suitable for table layout item mapping. (Ex: Transaction Details)

And each spec can have multiple rules. Here is a example.
```
{
  "headerSpec": {
    "rules": []
  },
  "detailSpec": {
    "rules": []
  }
}
```

### Header Spec
There are two ways to define rules for item mapping:
- Keyword: Extract text by using regular expression and mapping items
- Position: Extract text by position information and mapping items

`Position` type is a better way to use if PDF file format is fixed and hardly changed. Otherwise, use `Keyword` type since it is nomally more flexible to extract text information from PDF.

#### Keyword Type
```
・・・
"rules": [
  {
    "key": "submit_date",   # Unique key specify by users
    "type": "KEYWORD",      # Type of item mapping way
    "regexp": "Date.*(\\d{4}/\\d{1,2}/\\d{1,2})"  # Regular expression
  }
]
・・・
```
Text information on PDF file is consist of lines (Each line is String). Rules are applied for each lines and check whether matched pattern defined on `regexp` exists or not. Note that first matched value is always returned as result, even though there are several matched pattern on PDF.


#### Position Type
```
・・・
"rules": [
  {
    "key": "invoice_no",      # Unique key specify by users
    "type": "POSITION",       # Type of item mapping way	
    "page": 1,                # Page number
    "position": {             # Position of item
      "left": 161,
      "top": 171,
      "width": 103,
      "height": 26
    }
  }
]
・・・
```
If you are Mac user, you can use Preview to check item position. Open PDF file with Preview and press `command + i`. A new dialog is popped up. Now, you can select the items by dragging, then item position is shown on the dialog.

### Detail Spec
Default spec requires to configure the following settings.
| Key | Detail | Default Value |
| ---- | ---- | ---- |
| startPage | The number of start page.| 0 (Required) |
| endPage | The number of end page. | The number of pages of PDF file (Optional) |
| pageRules | Rules for each pages. Each rule will be applied to a specific single page | Empty ArrayList (Required) |
| defaultPageRule | Default page rule. This will be applied if page rule does not exist. (Optional) | None (Optional) |

#### Page Rule
In page rule, you need to specify the target page to extract. Then, start and end line top position are required to tell the range of table layout. In side of `rules`, only the first row of the table should be defined. The number of rows are automatically calculated with the start/end line position and the height of item position. `(endLine - startLine) / height of each rule`.
```
・・・
"pageRules": [
  {
    "page": 1,          # Page number
    "startLine": 185,   # Top position of start line (Used as top position of rules)
    "endLine": 680,     # Top position of end line
    "rules": [          # Rules (Detail rule only supports POSITION type)
      {
        "key": "detail_no",
        "position": {
          "left": 60,
          "width": 40,
          "height": 20
        }
      }
    ]
  }
]
・・・
```
Note that top position of each rule is not required.


#### Default Page Rule
Default Page Rule is almost same as Page Rule. Only difference is that you don't need to define `page`. Because this rule is applied to all pages that page rule does not exist. This is expected to be used when table layout continues over multiple pages.
```
・・・
"defaultPageRule": [
  {
    "startLine": 185,   # Top position of start line (Used as top position of rules)
    "endLine": 680,     # Top position of end line
    "rules": [          # Rules (Detail rule only supports POSITION type)
      {
        "key": "detail_no",
        "position": {
          "left": 60,
          "width": 40,
          "height": 20
        }
      }
    ]
  }
]
・・・
```

### Advanced Configuration
For each rules, the following features are supported.
| Key | Detail | Default Value |
| ---- | ---- | ---- |
| trim | Checks this unicode value before and after the string, if it exists then removes the spaces and returns the omitted string. | TRUE |
| onlyNumber | Check value and removes all chars except numbers (0-9), then returns the string only includes numbers | FALSE |
| noLineBreak | Check value and removes ¥n and ¥r, then returns the omitted string. | TRUE |

## Requirements
- Java8
- Maven3

## Planned Interface
You need to pass two arguments:
- ItemMapperConfig: Config class based on configuration json
- InputStream: Input stream of pdf file

Then, returning structured data as `ItemMapperResult`. Please check `ItemMapper` class for more detail.

## Directory Structure
```bash
$ tree -L 2
.
├── app
│   ├── App.java                  # Main Class
│   ├── ItemMapper.java           # Interface for other projects
│   ├── PDFItemMapper.java        # Implmenet class for other projects
│   └── PDFItemMapperConfig.java  # Config to be imported
├── config                        # Classes for settings
│   ├── DetailPageRule.java
│   ├── DetailRule.java
│   ├── DetailSpec.java
│   ├── ExtractorType.java
│   ├── HeaderRule.java
│   ├── HeaderSpec.java
│   ├── ItemMapperConfig.java
│   ├── ItemMapperResult.java
│   ├── Position.java
│   └── TextExtractorSpec.java
└── service                       # Classes for services
    ├── DetailItemMapperService.java
    ├── DetailItemMapperServiceImpl.java
    ├── HeaderItemMapperService.java
    ├── HeaderItemMapperServiceImpl.java
    ├── TextExtractorService.java
    └── TextExtractorServiceImpl.java
```
