package yan.tools

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

    static generateTemplate(File source,File baseDir){

        log("Start generate template from : $source.absolutePath")

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
                            .replaceAll(ClassUtil.propertyName(sourceClass),Matcher.quoteReplacement('${propertyName}'))
                }
            }
        }
    }

    static generateCode(File source,File baseDir){

        log("Start generate code from template for $source.absolutePath")

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
                    targetFile << engine.createTemplate(it).make(binding)
                }
            }
        }
    }

    static boolean isTargetFile(File file){
        return file.name.endsWith(".java") || file.name.endsWith(".groovy")
    }

    static log(String s){
        println s
    }
}
