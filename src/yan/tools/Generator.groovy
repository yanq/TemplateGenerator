package yan.tools

import com.intellij.openapi.ui.Messages
import groovy.text.SimpleTemplateEngine
import yan.tools.util.ClassUtil

import java.util.regex.Matcher

/**
 * Created by yan on 2017/2/25.
 */
class Generator {
    static final TEMPLATE_DIR = 'template'
    private static SimpleTemplateEngine engine = new SimpleTemplateEngine()
    private static GroovyClassLoader loader = new GroovyClassLoader()
    public static File logFile = File.createTempFile("GenerateLog",'txt')
    static {
        log("LogFile :$logFile.canonicalPath")
    }

    static generateTemplate(File source,File baseDir){

        log("Start generate template from : $source.absolutePath")

        ClassUtil.loadClassDir(source.parentFile,loader)
        Class sourceClass = loader.parseClass(source)
        File parentDir = new File(source.canonicalPath.substring(0,source.canonicalPath.lastIndexOf(ClassUtil.classPath(sourceClass))))
        parentDir.eachFileRecurse {
            if (it.canonicalPath == source.canonicalPath) return
            def relationPath = it.canonicalPath.replaceAll(Matcher.quoteReplacement(parentDir.canonicalPath),'')
            if (relationPath.contains(ClassUtil.shortName(sourceClass))) relationPath = relationPath.replaceAll(ClassUtil.shortName(sourceClass),Matcher.quoteReplacement('${name}'))
            File targetFile = new File(baseDir,"/$TEMPLATE_DIR${relationPath}")
            if (it.directory){
                targetFile.mkdirs()
            }else {
                if (!targetFile.parentFile.exists()) targetFile.parentFile.mkdirs()
                if (targetFile.exists()){
                    log("Exist file,pass : $targetFile.canonicalPath")
                }else {
                    log("Generate template to : $targetFile.canonicalPath")
                    targetFile << it.text
                            .replaceAll(ClassUtil.shortName(sourceClass),Matcher.quoteReplacement('${name}'))
                            .replaceAll(ClassUtil.propertyName(sourceClass),Matcher.quoteReplacement('${propertyName}')).getBytes('utf-8')
                }
            }
        }
    }

    static generateCode(File source,File baseDir){

        log("Start generate code from template for $source.absolutePath")

        ClassUtil.loadClassDir(source.parentFile,loader)
        Class theClass = loader.parseClass(source)
        File parentDir = new File(baseDir,TEMPLATE_DIR)
        parentDir.eachFileRecurse {
            def relationPath = it.canonicalPath.replaceAll(Matcher.quoteReplacement(parentDir.canonicalPath),'')
            if (relationPath.contains('${name}')) relationPath = relationPath.replaceAll('\\$\\{name\\}',ClassUtil.shortName(theClass))
            def binding = [theClass:theClass,name:ClassUtil.shortName(theClass),propertyName:ClassUtil.propertyName(theClass)]
            File targetFile = new File(source.canonicalPath.substring(0,source.canonicalPath.lastIndexOf(ClassUtil.classPath(theClass))),relationPath)
            if (it.directory){
                targetFile.mkdirs()
            }else {
                if (targetFile.exists()){
                    log("Exist file,pass : $targetFile.canonicalPath")
                }else {
                    log("Generate code to : $targetFile.canonicalPath")
                    targetFile << engine.createTemplate(it.text).make(binding).toString().getBytes('utf-8')
                }
            }
        }
    }

    static boolean isTargetFile(File file){
        return ClassUtil.isJavaClass(file)
    }

    static log(String s){
        def now = new Date().format("yyyy-MM-dd HH:mm:ss.SSS")
        def message = "$now $s \n"
        print message
        logFile << message
    }

    static String logText(){
        return logFile.text
    }
}
