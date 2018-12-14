package io.jenkins.plugins.rtl;

import com.google.common.collect.Lists;
import hudson.Extension;
import io.jenkins.plugins.coverage.adapter.parser.CoverageParser;
import io.jenkins.plugins.coverage.adapter.CoverageReportAdapter;
import io.jenkins.plugins.coverage.adapter.CoverageReportAdapterDescriptor;
import io.jenkins.plugins.coverage.adapter.XMLCoverageReportAdapter;
import io.jenkins.plugins.coverage.exception.CoverageException;
import io.jenkins.plugins.coverage.targets.CoverageElement;
import io.jenkins.plugins.coverage.targets.CoverageResult;
import io.jenkins.plugins.coverage.targets.Ratio;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.annotation.Nonnull;
import java.util.List;

public final class RtlCovReportAdapter extends XMLCoverageReportAdapter {

    @DataBoundConstructor
    public RtlCovReportAdapter(String path) {
        super(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getXSL() {
        return "rtl-cov-to-standard.xsl";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getXSD() {
        return null;
    }

    @Override
    public CoverageResult parseToResult(Document document, String reportName) throws CoverageException {
        return new RtlCovCoverageParser(reportName).parse(document);
    }


    @Symbol(value = {"rtlCovAdapter", "rtlCov"})
    @Extension
    public static final class RtlCovReportAdapterDescriptor extends CoverageReportAdapterDescriptor<CoverageReportAdapter> {

        public RtlCovReportAdapterDescriptor() {
            super(RtlCovReportAdapter.class);
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.RtlCovReportAdapter_displayName();
        }

        @Override
        public List<CoverageElement> getCoverageElements() {
            List<CoverageElement> registerCoverageElements = Lists.newArrayList(
                new CoverageElement("Metric", 0));
            // registerCoverageElements.add(new CoverageElement("Module", 1));
            registerCoverageElements.add(new CoverageElement("line", 2));
            registerCoverageElements.add(new CoverageElement("cond", 3));
            registerCoverageElements.add(new CoverageElement("toggle", 4));
            registerCoverageElements.add(new CoverageElement("branch", 5));
            registerCoverageElements.add(new CoverageElement("assert", 6));
            registerCoverageElements.add(new CoverageElement("fsm", 7));
            registerCoverageElements.add(new CoverageElement("other", 8));
            return registerCoverageElements;
        }
    }


    public static final class RtlCovCoverageParser extends CoverageParser {

        public RtlCovCoverageParser(String reportName) {
            super(reportName);
        }


        @Override
        protected CoverageResult processElement(Element current, CoverageResult parentResult) {
            CoverageResult result = null;
            switch (current.getLocalName()) {
                case "report":
                    result = new CoverageResult(CoverageElement.get("Report"), null,
                            getAttribute(current, "name", "") + ": " + getReportName());
                    break;
                case "metric":
                    result = new CoverageResult(CoverageElement.get("Metric"), parentResult,
                            getAttribute(current, "name", ""));
                    break;
                case "module":
                    result = new CoverageResult(CoverageElement.get(parentResult.getName()), parentResult,
                            getAttribute(current, "name", ""));
                    break;
                // case "line":
                //     result = new CoverageResult(CoverageElement.get("Line"), parentResult,
                //             getAttribute(current, "name", ""));
                //     break;
                // case "cond":
                //     result = new CoverageResult(CoverageElement.get("Conditional"), parentResult,
                //             getAttribute(current, "name", ""));
                //     break;
                // case "toggle":
                //     result = new CoverageResult(CoverageElement.get("Toggle"), parentResult,
                //             getAttribute(current, "name", ""));
                //     break;
                // case "branch":
                //     result = new CoverageResult(CoverageElement.get("Branch"), parentResult,
                //             getAttribute(current, "name", ""));
                //     break;
                // case "assertion":
                //     result = new CoverageResult(CoverageElement.get("Assertion"), parentResult,
                //             getAttribute(current, "name", ""));
                //     break;
                // case "fsm":
                //     result = new CoverageResult(CoverageElement.get("FSM"), parentResult,
                //             getAttribute(current, "name", ""));
                //     break;
                // case "other":
                //     result = new CoverageResult(CoverageElement.get("Other"), parentResult,
                //             getAttribute(current, "name", ""));
                //     break;
                default:
                    break;
            }

            if (getAttribute(current, "attr-mode", null) != null) {
                String lineCoveredAttr = getAttribute(current, "line-covered");
                String lineMissedAttr = getAttribute(current, "line-missed");

                String condCoveredAttr = getAttribute(current, "cond-covered");
                String condMissedAttr = getAttribute(current, "cond-missed");

                String toggleCoveredAttr = getAttribute(current, "toggle-covered");
                String toggleMissedAttr = getAttribute(current, "toggle-missed");

                String branchCoveredAttr = getAttribute(current, "branch-covered");
                String branchMissedAttr = getAttribute(current, "branch-missed");

                String assertionCoveredAttr = getAttribute(current, "assertion-covered");
                String assertionMissedAttr = getAttribute(current, "assertion-missed");

                String fsmCoveredAttr = getAttribute(current, "fsm-covered");
                String fsmMissedAttr = getAttribute(current, "fsm-missed");

                String otherCoveredAttr = getAttribute(current, "other-covered");
                String otherMissedAttr = getAttribute(current, "other-missed");

                if (StringUtils.isNumeric(lineCoveredAttr) && StringUtils.isNumeric(lineMissedAttr)) {
                    int covered = Integer.parseInt(lineCoveredAttr);
                    int missed = Integer.parseInt(lineMissedAttr);

                    result.updateCoverage(CoverageElement.get("line"), Ratio.create(covered, covered + missed));
                }

                if (StringUtils.isNumeric(condCoveredAttr) && StringUtils.isNumeric(condMissedAttr)) {
                    int covered = Integer.parseInt(condCoveredAttr);
                    int missed = Integer.parseInt(condMissedAttr);

                    result.updateCoverage(CoverageElement.get("cond"), Ratio.create(covered, covered + missed));
                }

                if(StringUtils.isNumeric(toggleCoveredAttr) && StringUtils.isNumeric(toggleMissedAttr)) {
                    int covered = Integer.parseInt(toggleCoveredAttr);
                    int missed = Integer.parseInt(toggleMissedAttr);

                    result.updateCoverage(CoverageElement.get("toggle"), Ratio.create(covered, covered + missed));
                }

                if(StringUtils.isNumeric(branchCoveredAttr) && StringUtils.isNumeric(branchMissedAttr)) {
                    int covered = Integer.parseInt(branchCoveredAttr);
                    int missed = Integer.parseInt(branchMissedAttr);

                    result.updateCoverage(CoverageElement.get("branch"), Ratio.create(covered, covered + missed));
                }

                if(StringUtils.isNumeric(assertionCoveredAttr) && StringUtils.isNumeric(assertionMissedAttr)) {
                    int covered = Integer.parseInt(assertionCoveredAttr);
                    int missed = Integer.parseInt(assertionMissedAttr);

                    result.updateCoverage(CoverageElement.get("assert"), Ratio.create(covered, covered + missed));
                }

                if(StringUtils.isNumeric(fsmCoveredAttr) && StringUtils.isNumeric(fsmMissedAttr)) {
                    int covered = Integer.parseInt(fsmCoveredAttr);
                    int missed = Integer.parseInt(fsmMissedAttr);

                    result.updateCoverage(CoverageElement.get("fsm"), Ratio.create(covered, covered + missed));
                }

                if(StringUtils.isNumeric(otherCoveredAttr) && StringUtils.isNumeric(otherMissedAttr)) {
                    int covered = Integer.parseInt(otherCoveredAttr);
                    int missed = Integer.parseInt(otherMissedAttr);

                    result.updateCoverage(CoverageElement.get("other"), Ratio.create(covered, covered + missed));
                }

            }

            return result;
        }
    }
}