# rtl-cov-plugin

This plugin allows you to integrate [RTL](https://en.wikipedia.org/wiki/Register-transfer_level) code coverage reports into
Jenkins. There is no standard format for RTL code coverage reports. Each [HDL](https://en.wikipedia.org/wiki/Hardware_description_language) compiler has its own unique format. This plugin is an implementation of the
[code-coverage-api](https://github.com/jenkinsci/code-coverage-api-plugin) plugin. To ease Jenkins integration and due to the
lack of standard RTL coverage report format, the rtl-cov-plugin expects the report to be in an XML format similar to the
standard format defined by the [code-coverage-api](https://github.com/jenkinsci/code-coverage-api-plugin) plugin. This means
that any tool-specific reports need to be parsed and converted to the XML format expected by this plugin.

## Report example:
```
<report name="myProjectName">
    <metric name="line">
        <module attr-mode="true" name="myModule1"
            line-covered="36" line-missed="0"/>
        <module attr-mode="true" name="myModule2"
            line-covered="40" line-missed="0"/>
        <module attr-mode="true" name="myModule3"
            line-covered="861" line-missed="0"/>
    </metric>
    <metric name="cond">
        <module attr-mode="true" name="myOtherModule1"
            cond-covered="62" cond-missed="2"/>
        <module attr-mode="true" name="myOtherModule2"
            cond-covered="2987" cond-missed="7"/>
        <module attr-mode="true" name="myOtherModule3"
            cond-covered="74" cond-missed="0"/>
    </metric>
    <metric name="toggle">
        <module attr-mode="true" name="myToggleModule1"
            toggle-covered="371" toggle-missed="14"/>
        <module attr-mode="true" name="myToggleModule2"
            toggle-covered="44" toggle-missed="0"/>
        <module attr-mode="true" name="myToggleModule3"
            toggle-covered="46" toggle-missed="0"/>
    </metric>
</report>
```
### Report format notes:
The plugin supports the most common RTL code coverage metrics:
* Line
* Conditional
* Branch
* Toggle
* Assertion
* Finite State Machine
* Other (used for any project-specific metric you want to track).

If there is a metric you do not want to track, simply remove the attributes from the XML or set its value to any non-numeric character (e.g. `"--"`).

## How to use it

- Install the rtl-cov plugin (For now build locally
(see [here](https://wiki.jenkins.io/display/JENKINS/Plugin+tutorial#Plugintutorial-BuildingaPlugin) for instructions) and
 manually upload it to you Jenkins master)
- Configure your project's build script to generate a coverage report
- Use a script to parse and transform the coverage report into the expected XML format
- Enable "Publish Coverage Report" publisher in the Post-build Actions
- Add rtl-cov in "Publish Coverage Report" publisher and specify reports path.
- (Optional) Specify Thresholds of each metrics
- (Optional) Specify Source code storing level to enable source code navigation
