package cn.cug.zigui.zigui;

import cn.cug.zigui.util.kml.KmlLine;
import cn.cug.zigui.util.kml.KmlPoint;
import cn.cug.zigui.util.kml.KmlProperty;
import cn.cug.zigui.util.kml.ParsingKmlUtil;
import cn.cug.zigui.vo.VLocation;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.json.JSONUtil;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.aspectj.weaver.ast.Var;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@SpringBootTest
class ZiguiApplicationTests {

	@Test
	void contextLoads() throws IOException {
		String dir = "/Users/edoclin/Downloads";
		String fileName = "zigui.kml";
		Document document = XmlUtil.readXML(dir + "/" + fileName);
		NodeList coord = document.getDocumentElement().getElementsByTagName("coord");
		List<VLocation> list = CollUtil.list(Boolean.TRUE);
		for (int i = 0; i < coord.getLength(); i++) {
			String[] coordinate = coord.item(i).getTextContent().split(" ");
			VLocation vLocation = new VLocation();
			vLocation.setLatitude(Double.valueOf(coordinate[1]));
			vLocation.setLongitude(Double.valueOf(coordinate[0]));
			list.add(vLocation);
		}
		String tempFile = fileName + "." +RandomUtil.randomString(6) + ".json";
		File file = FileUtil.newFile(dir + "/" + tempFile);
		FileOutputStream fos = new FileOutputStream(file);
		IoUtil.write(fos, Boolean.TRUE, JSONUtil.toJsonStr(list).getBytes());
		System.out.println(tempFile);
	}

	@Test
	void test1() {
		KmlProperty kmlProperty;
		ParsingKmlUtil parsingKmlUtil =new ParsingKmlUtil();
		File file = new File("/Users/edoclin/Downloads/批量导出_20220530171351.kml");
		kmlProperty = parsingKmlUtil.parseKmlForJAK(file);
		assert kmlProperty != null;
		List<Coordinate> result = CollUtil.list(Boolean.TRUE);
		if (kmlProperty.getKmlPoints().size() > 0) {
			for (KmlPoint k : kmlProperty.getKmlPoints()) {
				System.out.println(k.getName());
				result.addAll(k.getPoints());
				System.out.println(JSONUtil.toJsonStr(result));
				result.clear();
			}
			System.out.println("点");
		}
		if (kmlProperty.getKmlLines().size() > 0) {
			for (KmlLine k : kmlProperty.getKmlLines()) {
				System.out.println(k.getName());
				result.addAll(k.getPoints());
				System.out.println(JSONUtil.toJsonStr(result));
				result.clear();
			}
			System.out.println("线");
		}
		if (kmlProperty.getKmlPoints().size() > 0) {
			for (KmlPoint k : kmlProperty.getKmlPoints()) {
				System.out.println(k.getName());
				result.addAll(k.getPoints());
				System.out.println(JSONUtil.toJsonStr(result));
				result.clear();
			}
			System.out.println("面");
		}
	}
}
