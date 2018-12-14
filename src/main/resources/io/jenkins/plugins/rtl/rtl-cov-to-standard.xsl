<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="/">
        <xsl:if test="/report">
            <xsl:apply-templates select="report"/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="/report">
        <report>
            <xsl:attribute name="name">
                <xsl:value-of select="@name"/>
            </xsl:attribute>
            <xsl:apply-templates select="metric"/>
        </report>
    </xsl:template>

    <xsl:template match="metric">
        <metric>
            <xsl:attribute name="name">
                <xsl:value-of select="@name"/>
            </xsl:attribute>
            <xsl:apply-templates select="module"/>
        </metric>
    </xsl:template>

    <xsl:template match="module">
        <module>
            <xsl:copy-of select="@*"/>
        </module>
    </xsl:template>

</xsl:stylesheet>
