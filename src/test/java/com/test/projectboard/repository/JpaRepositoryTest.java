package com.test.projectboard.repository;

import com.test.projectboard.config.JpaConfig;
import com.test.projectboard.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

//@ActiveProfiles("testdb")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(@Autowired ArticleRepository articleRepository,
                             @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select Test")
    @Test
    void givenTestDate_whenSelecting_thenWorksFine() {
        // given

        // when
        List<Article> articles = articleRepository.findAll();

        // then
        assertThat(articles)
                .isNotNull()
                .hasSize(0);

    }

    @DisplayName("insert Test")
    @Test
    void givenTestDate_whenInserting_thenWorksFine() {
        // given
        long previousCount = articleRepository.count();

        // when
        Article saveArticle = articleRepository.save(Article.of("new Title", "new content", "#spring"));

        // then
        assertThat(articleRepository.count()).isEqualTo(previousCount +1);
    }

    @DisplayName("update Test")
    @Test
    void givenTestDate_whenUpdating_thenWorksFine() {
        // given
        Article  article = articleRepository.save(Article.of("new Title", "new content", "#spring"));
        String updateHashtag = "#Springboot";
        article.setHashtag(updateHashtag);

        // when
        Article savedArticle = articleRepository.saveAndFlush(article);

        // then
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updateHashtag);
    }

    @DisplayName("delete Test")
    @Test
    void givenTestDate_whenDeleting_thenWorksFine() {
        // given
        Article article = articleRepository.save(Article.of("new Title", "new content", "#spring"));
//        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        int deletedCommentsSize = article.getArticleComments().size();

        // when
        articleRepository.delete(article);

        // then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentsSize);

    }
}