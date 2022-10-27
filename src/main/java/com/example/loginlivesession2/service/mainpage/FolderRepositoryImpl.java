package com.example.loginlivesession2.service.mainpage;

import com.example.loginlivesession2.entity.*;
import com.example.loginlivesession2.repository.FolderRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class FolderRepositoryImpl implements FolderRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Folder> findByKeyword(String keyword, Member member) {
        QFolder folder = QFolder.folder;
        QFolderTag folderTag = QFolderTag.folderTag;

        List<FolderTag> folderTagList = queryFactory.selectFrom(folderTag)
                .leftJoin(folderTag.folder,folder).on(folder.member.eq(member))
                .where(folder.folderName.contains(keyword)
                        .or(folderTag.tagName.contains(keyword))) //파일이름 or 태그 검색
                .orderBy(folder.date.desc()) // 날짜 내림차순 정렬
                .fetch();

        return folderTagList.stream()
                .map(FolderTag::getFolder)
                .distinct() // 중복제거
                .collect(Collectors.toList());
    }

    /*private BooleanBuilder search(String keyword){
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(hasText(keyword)){
            booleanBuilder.or(folder.folderName.contains(keyword));
        }
        if(hasText(keyword)){
            booleanBuilder.or(folderTag.tagName.contains(keyword));
        }
        return booleanBuilder;
    }
    // .where(search(keyword))
    */

    @Override
    public HashMap<String, Long> myTopTags(Member member) {
        QFolder folder = QFolder.folder;
        QFolderTag folderTag = QFolderTag.folderTag;

        List<Tuple> tuples = queryFactory
                .from(folderTag)
                .select(folderTag.tagName, folderTag.count())
                .leftJoin(folderTag.folder,folder).on(folder.member.eq(member))
                .groupBy(folderTag.tagName)
                .orderBy(folderTag.count().desc())
                .limit(5)
                .fetch();

        HashMap<String,Long> returnMap = new HashMap<>(); // count type 이 Long임
        for (Tuple tuple : tuples) {
            returnMap.put(tuple.get(0, String.class), tuple.get(1, Long.class));
        }
        return returnMap;
    }

}
