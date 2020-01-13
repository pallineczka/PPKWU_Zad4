package com.ppkwu.zad4;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Search {

    @RequestMapping("VCard/{name}")
    public String getVCard(@PathVariable String name) throws IOException {
        name.replaceAll(" ", "+");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://adm.edu.p.lodz.pl/").append("user/users.php?search=");
        stringBuilder.append(name);

        String url = stringBuilder.toString();

        Document document;
        document = Jsoup.parse(new URL(url), 10000);

        Elements html = document.select("div.userlist");
        Elements userPicture = html.select("img.userpicture");
        Elements userDetails = html.select("div.user-info");
        Elements userName = userDetails.select("h3");
        Elements userAcademicTitle = userDetails.select("h4");
        Elements userExtraInfo = userDetails.select("div.extra-info");


        List<String> pictures = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        List<String> extraInfo = new ArrayList<>();

        for (Element e : userPicture)
            pictures.add(e.attr("src"));
        for (Element e : userName)
            names.add(e.text());
        for (Element e : userAcademicTitle)
            titles.add(e.text());
        for (Element e : userExtraInfo)
            extraInfo.add(e.text());

        int listSize = pictures.size();
        List<String> VCard = new ArrayList<>();

        for (int i = 0; i < listSize - 1; i++) {
            String pic = "<img src='" + pictures.get(i) + "'>";
            VCard.add(pic + "\t\t" + names.get(i) + "\n" + titles.get(i) + "\n" + extraInfo.get(i));
            VCard.add("<br></br>");
        }

        return VCard.toString().replace("[", "").replace("]", "").replace(",", "");
    }

}
