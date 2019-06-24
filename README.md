## Introduction

Every framework draws criticisms. At times, the philiosophy behind it results in extreme fractions. Regardless, be it a critique or a supporter, one cannot deny the amount of thought required behind the creation of such frameworks.  

This is my attempt at "reinventing" various utilities which frameworks normally address for us. The purpose of this is to expose myself to the thought process and considerations when creating the building blocks of a framework.

### Modules

1. Configuration Loader

   When starting up applications, only properties (i.e. dependency URL in PROD vs Staging) related to the specific environment are used in combination with properties which are consistent throughout all environments (i.e. application port number). 
   - How is it that frameworks are able to load such configurations without the need of pre-defined POJOs?
   - How do you control inheritance when loading such configurations (i.e. environment specific overrides defaults)?
   - How do you also present the option to map logical blocks of configuration to POJOs?