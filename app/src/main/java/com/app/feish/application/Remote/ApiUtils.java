package com.app.feish.application.Remote;

public interface ApiUtils {

    public static final String BASE_URL = "http://feish.online/apis/";
    public static final String BASE_URLMAngoDB = "http://192.168.1.16:8080/PatientReport/";
    public static final String BASE_URL1 = "http://192.168.0.204:8080/my_app_cakephp/users/";

    public static final String JSONstructre="[\n" +
            "    {\n" +
            "        \"_id\": {\n" +
            "            \"timestamp\": 1533908480,\n" +
            "            \"machineIdentifier\": 4002372,\n" +
            "            \"processIdentifier\": 18586,\n" +
            "            \"counter\": 9702421,\n" +
            "            \"timeSecond\": 1533908480,\n" +
            "            \"date\": 1533908480000,\n" +
            "            \"time\": 1533908480000\n" +
            "        },\n" +
            "        \"resourceType\": \"DiagnosticReport\",\n" +
            "        \"id\": \"798\",\n" +
            "        \"text\": {\n" +
            "            \"status\": \"generated\",\n" +
            "            \"div\": \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative with Details</b></p><p><b>id</b>: report</p><p><b>contained</b>: , , , , , , , , , , , , </p><p><b>status</b>: final</p><p><b>code</b>: comprehensive bone marrow report <span>(Details )</span></p><p><b>subject</b>: <a>Everywoman, Eve A, MRN: 123456789</a></p><p><b>effective</b>: 02/01/2015 1:20:10 PM</p><p><b>issued</b>: 06/01/2015 1:20:10 PM</p><p><b>performer</b>: <a>Molecular Diagnostic Laboratory</a></p><p><b>specimen</b>: <a>Bone marrow ID: MLD45-Z4-1234</a></p><p><b>result</b>: </p><ul><li>id: o1; status: final; Leukocytes [#/volume] in Blood <span>(Details : {LOINC code '26464-8' = 'Leukocytes [#/volume] in Blood', given as 'Leukocytes [#/volume] in Blood'})</span>; 31.9 K/ul</li><li>id: o2; status: final; Hematocrit [Volume Fraction] of Blood <span>(Details : {LOINC code '20570-8' = 'Hematocrit [Volume Fraction] of Blood', given as 'Hematocrit [Volume Fraction] of Blood'})</span>; 27 %</li><li>id: o3; status: final; Platelets [#/volume] in Blood <span>(Details : {LOINC code '26515-7' = 'Platelets [#/volume] in Blood', given as 'Platelets [#/volume] in Blood'})</span>; 84 K/ul</li><li>id: o4; status: final; NPM1 gene mutations found [Identifier] in Bone marrow by Molecular genetics method Nominal <span>(Details : {LOINC code '75034-9' = 'NPM1 gene mutations found [Identifier] in Bone marrow by Molecular genetics method Nominal', given as 'NPM1 gene mutations found [Identifier] in Bone marrow by Molecular genetics method Nominal'})</span>; Positive <span>(Details : {SNOMED CT code '10828004' = 'Positive (qualifier value)', given as 'Positive'})</span></li><li>id: o5; status: final; FLT3 gene mutation analysis in Bone marrow by Molecular genetics method Narrative <span>(Details : {LOINC code '54447-8' = 'FLT3 gene mutation analysis in Bone marrow by Molecular genetics method Narrative', given as 'FLT3 gene mutation analysis in Bone marrow by Molecular genetics method Narrative'})</span>; Negative <span>(Details : {SNOMED CT code '260385009' = 'Negative (qualifier value)', given as 'Negative'})</span></li><li>id: o6; status: final; KIT gene mutation analysis in Bone marrow by Molecular genetics method Narrative <span>(Details : {LOINC code '55201-8' = 'KIT gene mutation analysis in Blood or Tissue by Molecular genetics method Narrative', given as 'KIT gene mutation analysis in Bone marrow by Molecular genetics method Narrative'})</span>; Negative <span>(Details : {SNOMED CT code '260385009' = 'Negative (qualifier value)', given as 'Negative'})</span></li><li>id: o7; status: final; Myeloblasts [#/volume] in Blood <span>(Details : {LOINC code '30444-4' = 'Myeloblasts [#/volume] in Blood', given as 'Myeloblasts [#/volume] in Blood'})</span>; Positive <span>(Details : {SNOMED CT code '10828004' = 'Positive (qualifier value)', given as 'Positive'})</span></li><li>id: o8; status: final; CD4 Ag [Presence] in Tissue by Immune stain <span>(Details : {LOINC code '47016-1' = 'CD4 Ag [Presence] in Tissue by Immune stain', given as 'CD4 Ag [Presence] in Tissue by Immune stain'})</span>; Positive <span>(Details : {SNOMED CT code '10828004' = 'Positive (qualifier value)', given as 'Positive'})</span></li><li>id: o9; status: final; CD13 Ag [Presence] in Tissue by Immune stain <span>(Details : {LOINC code '49464-1' = 'CD13 Ag [Presence] in Tissue by Immune stain', given as 'CD13 Ag [Presence] in Tissue by Immune stain'})</span>; Positive <span>(Details : {SNOMED CT code '10828004' = 'Positive (qualifier value)', given as 'Positive'})</span></li><li>id: o10; status: final; CD2 Ag [Presence] in Tissue by Immune stain <span>(Details : {LOINC code '49466-6' = 'CD2 Ag [Presence] in Tissue by Immune stain', given as 'CD2 Ag [Presence] in Tissue by Immune stain'})</span>; Negative <span>(Details : {SNOMED CT code '260385009' = 'Negative (qualifier value)', given as 'Negative'})</span></li><li>id: o11; status: final; Karyotype [Identifier] in Bone marrow Nominal <span>(Details : {LOINC code '33893-9' = 'Karyotype [Identifier] in Bone marrow Nominal', given as 'Karyotype [Identifier] in Bone marrow Nominal'})</span>; 46,XX[20] <span>(Details )</span></li><li>id: o12; status: final; MDS-associated genetic abnormalities <span>(Details )</span>; Negative <span>(Details : {SNOMED CT code '260385009' = 'Negative (qualifier value)', given as 'Negative'})</span></li><li>id: o13; status: final; AML-associated genetic abnormalities <span>(Details )</span>; Negative <span>(Details : {SNOMED CT code '260385009' = 'Negative (qualifier value)', given as 'Negative'})</span></li></ul></div>\"\n" +
            "        },\n" +
            "        \"contained\": [\n" +
            "            {\n" +
            "                \"resourceType\": \"Observation\",\n" +
            "                \"id\": \"o1\",\n" +
            "                \"status\": \"final\",\n" +
            "                \"code\": {\n" +
            "                    \"coding\": [\n" +
            "                        {\n" +
            "                            \"system\": \"http://loinc.org\",\n" +
            "                            \"code\": \"26464-8\",\n" +
            "                            \"display\": \"Leukocytes [#/volume] in Blood\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                },\n" +
            "                \"valueQuantity\": {\n" +
            "                    \"value\": 31.9,\n" +
            "                    \"unit\": \"K/ul\"\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"resourceType\": \"Observation\",\n" +
            "                \"id\": \"o2\",\n" +
            "                \"status\": \"final\",\n" +
            "                \"code\": {\n" +
            "                    \"coding\": [\n" +
            "                        {\n" +
            "                            \"system\": \"http://loinc.org\",\n" +
            "                            \"code\": \"20570-8\",\n" +
            "                            \"display\": \"Hematocrit [Volume Fraction] of Blood\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                },\n" +
            "                \"valueQuantity\": {\n" +
            "                    \"value\": 27,\n" +
            "                    \"unit\": \"%\"\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"resourceType\": \"Observation\",\n" +
            "                \"id\": \"o3\",\n" +
            "                \"status\": \"final\",\n" +
            "                \"code\": {\n" +
            "                    \"coding\": [\n" +
            "                        {\n" +
            "                            \"system\": \"http://loinc.org\",\n" +
            "                            \"code\": \"26515-7\",\n" +
            "                            \"display\": \"Platelets [#/volume] in Blood\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                },\n" +
            "                \"valueQuantity\": {\n" +
            "                    \"value\": 84,\n" +
            "                    \"unit\": \"K/ul\"\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"resourceType\": \"Observation\",\n" +
            "                \"id\": \"o4\",\n" +
            "                \"status\": \"final\",\n" +
            "                \"code\": {\n" +
            "                    \"coding\": [\n" +
            "                        {\n" +
            "                            \"system\": \"http://loinc.org\",\n" +
            "                            \"code\": \"75034-9\",\n" +
            "                            \"display\": \"NPM1 gene mutations found [Identifier] in Bone marrow by Molecular genetics method Nominal\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                },\n" +
            "                \"valueCodeableConcept\": {\n" +
            "                    \"coding\": [\n" +
            "                        {\n" +
            "                            \"system\": \"http://snomed.info/sct\",\n" +
            "                            \"code\": \"10828004\",\n" +
            "                            \"display\": \"Positive\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"resourceType\": \"Observation\",\n" +
            "                \"id\": \"o5\",\n" +
            "                \"status\": \"final\",\n" +
            "                \"code\": {\n" +
            "                    \"coding\": [\n" +
            "                        {\n" +
            "                            \"system\": \"http://loinc.org\",\n" +
            "                            \"code\": \"54447-8\",\n" +
            "                            \"display\": \"FLT3 gene mutation analysis in Bone marrow by Molecular genetics method Narrative\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                },\n" +
            "                \"valueCodeableConcept\": {\n" +
            "                    \"coding\": [\n" +
            "                        {\n" +
            "                            \"system\": \"http://snomed.info/sct\",\n" +
            "                            \"code\": \"260385009\",\n" +
            "                            \"display\": \"Negative\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"resourceType\": \"Observation\",\n" +
            "                \"id\": \"o6\",\n" +
            "                \"status\": \"final\",\n" +
            "                \"code\": {\n" +
            "                    \"coding\": [\n" +
            "                        {\n" +
            "                            \"system\": \"http://loinc.org\",\n" +
            "                            \"code\": \"55201-8\",\n" +
            "                            \"display\": \"KIT gene mutation analysis in Bone marrow by Molecular genetics method Narrative\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                },\n" +
            "                \"valueCodeableConcept\": {\n" +
            "                    \"coding\": [\n" +
            "                        {\n" +
            "                            \"system\": \"http://snomed.info/sct\",\n" +
            "                            \"code\": \"260385009\",\n" +
            "                            \"display\": \"Negative\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"resourceType\": \"Observation\",\n" +
            "                \"id\": \"o7\",\n" +
            "                \"status\": \"final\",\n" +
            "                \"code\": {\n" +
            "                    \"coding\": [\n" +
            "                        {\n" +
            "                            \"system\": \"http://loinc.org\",\n" +
            "                            \"code\": \"30444-4\",\n" +
            "                            \"display\": \"Myeloblasts [#/volume] in Blood\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                },\n" +
            "                \"valueCodeableConcept\": {\n" +
            "                    \"coding\": [\n" +
            "                        {\n" +
            "                            \"system\": \"http://snomed.info/sct\",\n" +
            "                            \"code\": \"10828004\",\n" +
            "                            \"display\": \"Positive\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"resourceType\": \"Observation\",\n" +
            "                \"id\": \"o8\",\n" +
            "                \"status\": \"final\",\n" +
            "                \"code\": {\n" +
            "                    \"coding\": [\n" +
            "                        {\n" +
            "                            \"system\": \"http://loinc.org\",\n" +
            "                            \"code\": \"47016-1\",\n" +
            "                            \"display\": \"CD4 Ag [Presence] in Tissue by Immune stain\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                },\n" +
            "                \"valueCodeableConcept\": {\n" +
            "                    \"coding\": [\n" +
            "                        {\n" +
            "                            \"system\": \"http://snomed.info/sct\",\n" +
            "                            \"code\": \"10828004\",\n" +
            "                            \"display\": \"Positive\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"resourceType\": \"Observation\",\n" +
            "                \"id\": \"o9\",\n" +
            "                \"status\": \"final\",\n" +
            "                \"code\": {\n" +
            "                    \"coding\": [\n" +
            "                        {\n" +
            "                            \"system\": \"http://loinc.org\",\n" +
            "                            \"code\": \"49464-1\",\n" +
            "                            \"display\": \"CD13 Ag [Presence] in Tissue by Immune stain\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                },\n" +
            "                \"valueCodeableConcept\": {\n" +
            "                    \"coding\": [\n" +
            "                        {\n" +
            "                            \"system\": \"http://snomed.info/sct\",\n" +
            "                            \"code\": \"10828004\",\n" +
            "                            \"display\": \"Positive\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"resourceType\": \"Observation\",\n" +
            "                \"id\": \"o10\",\n" +
            "                \"status\": \"final\",\n" +
            "                \"code\": {\n" +
            "                    \"coding\": [\n" +
            "                        {\n" +
            "                            \"system\": \"http://loinc.org\",\n" +
            "                            \"code\": \"49466-6\",\n" +
            "                            \"display\": \"CD2 Ag [Presence] in Tissue by Immune stain\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                },\n" +
            "                \"valueCodeableConcept\": {\n" +
            "                    \"coding\": [\n" +
            "                        {\n" +
            "                            \"system\": \"http://snomed.info/sct\",\n" +
            "                            \"code\": \"260385009\",\n" +
            "                            \"display\": \"Negative\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"resourceType\": \"Observation\",\n" +
            "                \"id\": \"o11\",\n" +
            "                \"status\": \"final\",\n" +
            "                \"code\": {\n" +
            "                    \"coding\": [\n" +
            "                        {\n" +
            "                            \"system\": \"http://loinc.org\",\n" +
            "                            \"code\": \"33893-9\",\n" +
            "                            \"display\": \"Karyotype [Identifier] in Bone marrow Nominal\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                },\n" +
            "                \"valueCodeableConcept\": {\n" +
            "                    \"text\": \"46,XX[20]\"\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"resourceType\": \"Observation\",\n" +
            "                \"id\": \"o12\",\n" +
            "                \"status\": \"final\",\n" +
            "                \"code\": {\n" +
            "                    \"text\": \"MDS-associated genetic abnormalities\"\n" +
            "                },\n" +
            "                \"valueCodeableConcept\": {\n" +
            "                    \"coding\": [\n" +
            "                        {\n" +
            "                            \"system\": \"http://snomed.info/sct\",\n" +
            "                            \"code\": \"260385009\",\n" +
            "                            \"display\": \"Negative\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"resourceType\": \"Observation\",\n" +
            "                \"id\": \"o13\",\n" +
            "                \"status\": \"final\",\n" +
            "                \"code\": {\n" +
            "                    \"text\": \"AML-associated genetic abnormalities\"\n" +
            "                },\n" +
            "                \"valueCodeableConcept\": {\n" +
            "                    \"coding\": [\n" +
            "                        {\n" +
            "                            \"system\": \"http://snomed.info/sct\",\n" +
            "                            \"code\": \"260385009\",\n" +
            "                            \"display\": \"Negative\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                }\n" +
            "            }\n" +
            "        ],\n" +
            "        \"extension\": [\n" +
            "            {\n" +
            "                \"url\": \"http://hl7.org/fhir/StructureDefinition/DiagnosticReport-geneticsAssessedCondition\",\n" +
            "                \"valueReference\": {\n" +
            "                    \"reference\": \"c1\"\n" +
            "                }\n" +
            "            }\n" +
            "        ],\n" +
            "        \"status\": \"final\",\n" +
            "        \"code\": {\n" +
            "            \"text\": \"comprehensive bone marrow report\"\n" +
            "        },\n" +
            "        \"subject\": {\n" +
            "            \"reference\": \"Patient/genetics-example1\",\n" +
            "            \"display\": \"Everywoman, Eve A, MRN: 123456789\"\n" +
            "        },\n" +
            "        \"effectiveDateTime\": \"2015-01-02T13:20:10+01:00\",\n" +
            "        \"issued\": \"2015-01-06T13:20:10+01:00\",\n" +
            "        \"performer\": [\n" +
            "            {\n" +
            "                \"reference\": \"Practitioner/genetics-example1\",\n" +
            "                \"display\": \"Molecular Diagnostic Laboratory\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"specimen\": [\n" +
            "            {\n" +
            "                \"reference\": \"Specimen/genetics-example1\",\n" +
            "                \"display\": \"Bone marrow ID: MLD45-Z4-1234\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"result\": [\n" +
            "            {\n" +
            "                \"reference\": \"#o1\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"reference\": \"#o2\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"reference\": \"#o3\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"reference\": \"#o4\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"reference\": \"#o5\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"reference\": \"#o6\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"reference\": \"#o7\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"reference\": \"#o8\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"reference\": \"#o9\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"reference\": \"#o10\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"reference\": \"#o11\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"reference\": \"#o12\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"reference\": \"#o13\"\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"_id\": {\n" +
            "            \"timestamp\": 1533908480,\n" +
            "            \"machineIdentifier\": 4002372,\n" +
            "            \"processIdentifier\": 18586,\n" +
            "            \"counter\": 9702422,\n" +
            "            \"timeSecond\": 1533908480,\n" +
            "            \"date\": 1533908480000,\n" +
            "            \"time\": 1533908480000\n" +
            "        },\n" +
            "        \"resourceType\": \"DiagnosticReport\",\n" +
            "        \"id\": \"798\",\n" +
            "        \"text\": {\n" +
            "            \"status\": \"generated\",\n" +
            "            \"div\": \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative with Details</b></p><p><b>id</b>: hla-1</p><p><b>status</b>: final</p><p><b>code</b>: HLA genotyping results <span>(Details )</span></p><p><b>subject</b>: <a>Molecular Lab Patient ID: HOSP-23456</a></p><p><b>effective</b>: 26/05/2015 3:30:10 PM</p><p><b>issued</b>: 26/05/2015 3:30:10 PM</p><p><b>performer</b>: <a>Molecular Diagnostic Laboratory</a></p><p><b>specimen</b>: <a>Molecular Specimen ID: MLD45-Z4-1234</a></p></div>\"\n" +
            "        },\n" +
            "        \"extension\": [\n" +
            "            {\n" +
            "                \"url\": \"http://hl7.org/fhir/StructureDefinition/hla-genotyping-resultsGlstring\",\n" +
            "                \"extension\": [\n" +
            "                    {\n" +
            "                        \"url\": \"text\",\n" +
            "                        \"valueString\": \"HLA-A*01:01:01+HLA-A*24:02:01\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "        ],\n" +
            "        \"status\": \"final\",\n" +
            "        \"code\": {\n" +
            "            \"text\": \"HLA genotyping results\"\n" +
            "        },\n" +
            "        \"subject\": {\n" +
            "            \"reference\": \"Patient/genetics-example2\",\n" +
            "            \"display\": \"Molecular Lab Patient ID: HOSP-23456\"\n" +
            "        },\n" +
            "        \"effectiveDateTime\": \"2015-05-26T15:30:10+01:00\",\n" +
            "        \"issued\": \"2015-05-26T15:30:10+01:00\",\n" +
            "        \"performer\": [\n" +
            "            {\n" +
            "                \"reference\": \"Practitioner/genetics-example2\",\n" +
            "                \"display\": \"Molecular Diagnostic Laboratory\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"specimen\": [\n" +
            "            {\n" +
            "                \"reference\": \"Specimen/genetics-example2\",\n" +
            "                \"display\": \"Molecular Specimen ID: MLD45-Z4-1234\"\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"_id\": {\n" +
            "            \"timestamp\": 1533908480,\n" +
            "            \"machineIdentifier\": 4002372,\n" +
            "            \"processIdentifier\": 18586,\n" +
            "            \"counter\": 9702423,\n" +
            "            \"timeSecond\": 1533908480,\n" +
            "            \"date\": 1533908480000,\n" +
            "            \"time\": 1533908480000\n" +
            "        },\n" +
            "        \"resourceType\": \"DiagnosticReport\",\n" +
            "        \"id\": \"798\",\n" +
            "        \"text\": {\n" +
            "            \"status\": \"generated\",\n" +
            "            \"div\": \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">\\n          \\n          \\n          <h3>Genetic test Report for Marry Chalmers (MRN: 12345) </h3>\\n          \\n          <pre>\\n              Gene                DNAVariation       Status        Interpretation\\n              BRCA1                                 Negative\\n              BRCA2                 185delAG        Positive         Pathogenic\\n          </pre>\\n          <p> Method: BRACAnalysis CDx Offered by Myriad Genetic Laboratories, Inc </p>\\n          <p> Issued: 2015-05-26T15:30:10+01:00</p>\\n          <p> FamilyMemeberHistory: Mother </p>\\n          <pre>\\n              Gene                 DNAVariation      Status\\n              BRCA2                 185delAG        Positive\\n          </pre>\\n          <p> Method: BRACAnalysis CDx Offered by Myriad Genetic Laboratories, Inc </p>\\n          \\n      </div>\"\n" +
            "        },\n" +
            "        \"contained\": [\n" +
            "            {\n" +
            "                \"resourceType\": \"FamilyMemberHistory\",\n" +
            "                \"id\": \"f1-genetics\",\n" +
            "                \"extension\": [\n" +
            "                    {\n" +
            "                        \"url\": \"http://hl7.org/fhir/StructureDefinition/family-member-history-genetics-observation\",\n" +
            "                        \"valueReference\": {\n" +
            "                            \"reference\": \"DiagnosticReport/dgf1\"\n" +
            "                        }\n" +
            "                    }\n" +
            "                ],\n" +
            "                \"patient\": {\n" +
            "                    \"reference\": \"Patient/example\"\n" +
            "                },\n" +
            "                \"status\": \"completed\",\n" +
            "                \"relationship\": {\n" +
            "                    \"coding\": [\n" +
            "                        {\n" +
            "                            \"system\": \"http://hl7.org/fhir/familial-relationship\",\n" +
            "                            \"code\": \"MTH\",\n" +
            "                            \"display\": \"Mother\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                }\n" +
            "            }\n" +
            "        ],\n" +
            "        \"extension\": [\n" +
            "            {\n" +
            "                \"url\": \"http://hl7.org/fhir/StructureDefinition/DiagnosticReport-geneticsFamilyMemberHistory\",\n" +
            "                \"valueReference\": {\n" +
            "                    \"reference\": \"#f1-genetics\"\n" +
            "                }\n" +
            "            }\n" +
            "        ],\n" +
            "        \"status\": \"final\",\n" +
            "        \"category\": {\n" +
            "            \"coding\": [\n" +
            "                {\n" +
            "                    \"system\": \"http://snomed.info/sct\",\n" +
            "                    \"code\": \"15220000\",\n" +
            "                    \"display\": \"Laboratory test\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"system\": \"http://hl7.org/fhir/v2/0074\",\n" +
            "                    \"code\": \"LAB\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        \"code\": {\n" +
            "            \"coding\": [\n" +
            "                {\n" +
            "                    \"system\": \"http://loinc.org\",\n" +
            "                    \"code\": \"55233-1\",\n" +
            "                    \"display\": \"Genetic analysis master panel\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        \"subject\": {\n" +
            "            \"reference\": \"Patient/example\",\n" +
            "            \"display\": \"Peter James Chalmers(MRN: 12345)\"\n" +
            "        },\n" +
            "        \"effectiveDateTime\": \"2015-05-26T15:30:10+01:00\",\n" +
            "        \"issued\": \"2014-05-16T10:28:00+01:00\",\n" +
            "        \"performer\": [\n" +
            "            {\n" +
            "                \"reference\": \"Practitioner/genetics-example2\",\n" +
            "                \"display\": \"Molecular Diagnostic Laboratory\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"specimen\": [\n" +
            "            {\n" +
            "                \"reference\": \"Specimen/genetics-example2\",\n" +
            "                \"display\": \"Molecular Specimen ID: MLD45-Z4-1234\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"result\": [\n" +
            "            {\n" +
            "                \"reference\": \"Observation/ob-genetics-3-1\",\n" +
            "                \"display\": \"Genetic analysis for BRAC -1\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"reference\": \"Observation/ob-genetics-3-2\",\n" +
            "                \"display\": \"Genetic analysis for BRAC -2\"\n" +
            "            }\n" +
            "        ]\n" +
            "    }\n" +
            "]";
    public static final String JSONstructre3="{\n" +
            "    \"_id\" : ObjectId(\"5b6d96003d1244489a940c17\"),\n" +
            "    \"resourceType\" : \"DiagnosticReport\",\n" +
            "    \"id\" : \"dg2\",\n" +
            "    \"text\" : {\n" +
            "        \"status\" : \"generated\",\n" +
            "        \"div\" : \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">\\n          \\n          \\n          <h3>Genetic test Report for Marry Chalmers (MRN: 12345) </h3>\\n          \\n          <pre>\\n              Gene                DNAVariation       Status        Interpretation\\n              BRCA1                                 Negative\\n              BRCA2                 185delAG        Positive         Pathogenic\\n          </pre>\\n          <p> Method: BRACAnalysis CDx Offered by Myriad Genetic Laboratories, Inc </p>\\n          <p> Issued: 2015-05-26T15:30:10+01:00</p>\\n          <p> FamilyMemeberHistory: Mother </p>\\n          <pre>\\n              Gene                 DNAVariation      Status\\n              BRCA2                 185delAG        Positive\\n          </pre>\\n          <p> Method: BRACAnalysis CDx Offered by Myriad Genetic Laboratories, Inc </p>\\n          \\n      </div>\"\n" +
            "    },\n" +
            "    \"contained\" : [ \n" +
            "        {\n" +
            "            \"resourceType\" : \"FamilyMemberHistory\",\n" +
            "            \"id\" : \"f1-genetics\",\n" +
            "            \"extension\" : [ \n" +
            "                {\n" +
            "                    \"url\" : \"http://hl7.org/fhir/StructureDefinition/family-member-history-genetics-observation\",\n" +
            "                    \"valueReference\" : {\n" +
            "                        \"reference\" : \"DiagnosticReport/dgf1\"\n" +
            "                    }\n" +
            "                }\n" +
            "            ],\n" +
            "            \"patient\" : {\n" +
            "                \"reference\" : \"Patient/example\"\n" +
            "            },\n" +
            "            \"status\" : \"completed\",\n" +
            "            \"relationship\" : {\n" +
            "                \"coding\" : [ \n" +
            "                    {\n" +
            "                        \"system\" : \"http://hl7.org/fhir/familial-relationship\",\n" +
            "                        \"code\" : \"MTH\",\n" +
            "                        \"display\" : \"Mother\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "        }\n" +
            "    ],\n" +
            "    \"extension\" : [ \n" +
            "        {\n" +
            "            \"url\" : \"http://hl7.org/fhir/StructureDefinition/DiagnosticReport-geneticsFamilyMemberHistory\",\n" +
            "            \"valueReference\" : {\n" +
            "                \"reference\" : \"#f1-genetics\"\n" +
            "            }\n" +
            "        }\n" +
            "    ],\n" +
            "    \"status\" : \"final\",\n" +
            "    \"category\" : {\n" +
            "        \"coding\" : [ \n" +
            "            {\n" +
            "                \"system\" : \"http://snomed.info/sct\",\n" +
            "                \"code\" : \"15220000\",\n" +
            "                \"display\" : \"Laboratory test\"\n" +
            "            }, \n" +
            "            {\n" +
            "                \"system\" : \"http://hl7.org/fhir/v2/0074\",\n" +
            "                \"code\" : \"LAB\"\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"code\" : {\n" +
            "        \"coding\" : [ \n" +
            "            {\n" +
            "                \"system\" : \"http://loinc.org\",\n" +
            "                \"code\" : \"55233-1\",\n" +
            "                \"display\" : \"Genetic analysis master panel\"\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"subject\" : {\n" +
            "        \"reference\" : \"Patient/example\",\n" +
            "        \"display\" : \"Peter James Chalmers(MRN: 12345)\"\n" +
            "    },\n" +
            "    \"effectiveDateTime\" : \"2015-05-26T15:30:10+01:00\",\n" +
            "    \"issued\" : \"2014-05-16T10:28:00+01:00\",\n" +
            "    \"performer\" : [ \n" +
            "        {\n" +
            "            \"reference\" : \"Practitioner/genetics-example2\",\n" +
            "            \"display\" : \"Molecular Diagnostic Laboratory\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"specimen\" : [ \n" +
            "        {\n" +
            "            \"reference\" : \"Specimen/genetics-example2\",\n" +
            "            \"display\" : \"Molecular Specimen ID: MLD45-Z4-1234\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"result\" : [ \n" +
            "        {\n" +
            "            \"reference\" : \"Observation/ob-genetics-3-1\",\n" +
            "            \"display\" : \"Genetic analysis for BRAC -1\"\n" +
            "        }, \n" +
            "        {\n" +
            "            \"reference\" : \"Observation/ob-genetics-3-2\",\n" +
            "            \"display\" : \"Genetic analysis for BRAC -2\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";
}
