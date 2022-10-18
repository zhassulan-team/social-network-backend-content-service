package kata.academy.eurekacontentservice.init;

import kata.academy.eurekacontentservice.model.entity.Comment;
import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.service.CommentService;
import kata.academy.eurekacontentservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
@Profile("dev")
public class ApplicationRunnerImpl implements ApplicationRunner {

    private final PostService postService;
    private final CommentService commentService;

    @Override
    public void run(ApplicationArguments args) {
        List<Post> posts = new ArrayList<>();
        Post post1 = new Post();
        post1.setUserId(1L);
        post1.setTitle("The Competition");
        post1.setText("A “Share” vs. “Like” post is a great way for people to engage with your brand by offering their " +
                "opinion (which Facebook users love to do!). Basically, this post offers two different options.");
        post1.setCreatedDate(LocalDateTime.now());
        post1.setTags(List.of("like", "post"));
        posts.add(post1);

        Post post2 = new Post();
        post2.setUserId(1L);
        post2.setTitle("The Poll");
        post2.setText("The poll is a simple but effective way to get your Fans to involve themselves in your brand. " +
                "This post includes 2-4 options, and then asks Fans for their favorite. It’s also a great way. ");
        post2.setCreatedDate(LocalDateTime.now());
        post2.setTags(List.of("fans", "poll"));
        posts.add(post2);

        Post post3 = new Post();
        post3.setUserId(1L);
        post3.setTitle("Ask for it");
        post3.setText("Sometimes the simplest, most straightforward idea is the best. Just like with Twitter, in which " +
                "asking for a retweet increases the chance of it being done by 23 times, being clear about.");
        post3.setCreatedDate(LocalDateTime.now());
        post3.setTags(List.of("ask", "reward"));
        posts.add(post3);

        Post post4 = new Post();
        post4.setUserId(2L);
        post4.setTitle("Give a sneak peek");
        post4.setText("Giving a sneak peek is one of the more complex Facebook product posts, but has a more lasting " +
                "draw. Simply find something your users haven’t seen yet and offer a preview");
        post4.setCreatedDate(LocalDateTime.now());
        post4.setTags(List.of("sneak", "peak"));
        posts.add(post4);

        Post post5 = new Post();
        post5.setUserId(2L);
        post5.setTitle("Offer Advice");
        post5.setText("A look at 682 Facebook posts found that posts containing advice or teaching something new were " +
                "shared 522% more than posts that weren’t advice related. Whether it’s a recipe, a statistic.");
        post5.setCreatedDate(LocalDateTime.now());
        post5.setTags(List.of("offer", "advice"));
        posts.add(post5);

        Post post6 = new Post();
        post6.setUserId(3L);
        post6.setTitle("Geo-Target your Audience");
        post6.setText("By intentionally limiting your target audience you create exclusivity, drawing in those ‘select’ " +
                "Fans who feel attached to your subject. Do this by providing a specific picture or hashtag.");
        post6.setCreatedDate(LocalDateTime.now());
        post6.setTags(List.of("geo-target", "facebook"));
        posts.add(post6);

        Post post7 = new Post();
        post7.setUserId(3L);
        post7.setTitle("Use Universal Appeal");
        post7.setText("Whether it’s an inspiring or funny quote, an easily-agreed-upon opinion, or an interesting fact.");
        post7.setCreatedDate(LocalDateTime.now());
        post7.setTags(List.of("appeal", "like"));
        posts.add(post7);

        Post post8 = new Post();
        post8.setUserId(3L);
        post8.setTitle("Showcase your Fans");
        post8.setText("Facebook is a conversation and showcasing your Fan’s talent is a great way to create an exclusive " +
                "community. Your Fans will respond more enthusiastically and more often if they feel you’re.");
        post8.setCreatedDate(LocalDateTime.now());
        post8.setTags(List.of("fans", "facebook"));
        posts.add(post8);

        Post post9 = new Post();
        post9.setUserId(4L);
        post9.setTitle("Go Adorable");
        post9.setText("A quick and easy way to create some Facebook attention is to use a cute or appealing image. In many " +
                "ways this strategy is similar to Coca-Cola’s ‘universal appeal’ technique. Simply find a picture.");
        post9.setCreatedDate(LocalDateTime.now());
        post9.setTags(List.of("adorable", "coca-cola"));
        posts.add(post9);

        Post post10 = new Post();
        post10.setUserId(4L);
        post10.setTitle("Use Real People");
        post10.setText("Using real people is one of the best ways to halt a Facebook user mid-scroll. People expect to see " +
                "celebrities and historical figures so a genuine face, with a story behind it, can be.");
        post10.setCreatedDate(LocalDateTime.now());
        post10.setTags(List.of("people", "facebook"));
        posts.add(post10);

        Post post11 = new Post();
        post11.setUserId(5L);
        post11.setTitle("Use Memes");
        post11.setText("Depending on your social media persona and tone, using a meme or humorous Ecard can be a great way " +
                "to engage with your Fans. Used wisely they work much like Coca-Cola’s inspirational quote and HP.");
        post11.setCreatedDate(LocalDateTime.now());
        post11.setTags(List.of("memes", "like"));
        posts.add(post11);

        Post post12 = new Post();
        post12.setUserId(5L);
        post12.setTitle("Check your Links");
        post12.setText("By prioritizing the editorial process and double-checking your work, you can limit those little human " +
                "errors that can make all the difference. But no matter how hard we try, the intricacies of social.");
        post12.setCreatedDate(LocalDateTime.now());
        post12.setTags(List.of("links", "media"));
        posts.add(post12);

        for (Post post : posts) {
            postService.addPost(post);
        }

        List<Comment> comments = new ArrayList<>();

        Comment comment1 = new Comment();
        comment1.setUserId(1L);
        comment1.setText(getRandomText());
        comment1.setCreatedDate(LocalDateTime.now());
        comment1.setPost(posts.get(0));
        comments.add(comment1);

        Comment comment2 = new Comment();
        comment2.setUserId(2L);
        comment2.setText(getRandomText());
        comment2.setCreatedDate(LocalDateTime.now());
        comment2.setPost(posts.get(1));
        comments.add(comment2);

        Comment comment3 = new Comment();
        comment3.setUserId(3L);
        comment3.setText(getRandomText());
        comment3.setCreatedDate(LocalDateTime.now());
        comment3.setPost(posts.get(1));
        comments.add(comment3);

        Comment comment4 = new Comment();
        comment4.setUserId(4L);
        comment4.setText(getRandomText());
        comment4.setCreatedDate(LocalDateTime.now());
        comment4.setPost(posts.get(2));
        comments.add(comment4);

        Comment comment5 = new Comment();
        comment5.setUserId(5L);
        comment5.setText(getRandomText());
        comment5.setCreatedDate(LocalDateTime.now());
        comment5.setPost(posts.get(2));
        comments.add(comment5);

        Comment comment6 = new Comment();
        comment6.setUserId(6L);
        comment6.setText(getRandomText());
        comment6.setCreatedDate(LocalDateTime.now());
        comment6.setPost(posts.get(2));
        comments.add(comment6);

        Comment comment7 = new Comment();
        comment7.setUserId(7L);
        comment7.setText(getRandomText());
        comment7.setCreatedDate(LocalDateTime.now());
        comment7.setPost(posts.get(3));
        comments.add(comment7);

        Comment comment8 = new Comment();
        comment8.setUserId(8L);
        comment8.setText(getRandomText());
        comment8.setCreatedDate(LocalDateTime.now());
        comment8.setPost(posts.get(4));
        comments.add(comment8);

        Comment comment9 = new Comment();
        comment9.setUserId(9L);
        comment9.setText(getRandomText());
        comment9.setCreatedDate(LocalDateTime.now());
        comment9.setPost(posts.get(4));
        comments.add(comment9);

        Comment comment10 = new Comment();
        comment10.setUserId(10L);
        comment10.setText(getRandomText());
        comment10.setCreatedDate(LocalDateTime.now());
        comment10.setPost(posts.get(5));
        comments.add(comment10);

        Comment comment11 = new Comment();
        comment11.setUserId(11L);
        comment11.setText(getRandomText());
        comment11.setCreatedDate(LocalDateTime.now());
        comment11.setPost(posts.get(5));
        comments.add(comment11);

        Comment comment12 = new Comment();
        comment12.setUserId(12L);
        comment12.setText(getRandomText());
        comment12.setCreatedDate(LocalDateTime.now());
        comment12.setPost(posts.get(5));
        comments.add(comment12);

        Comment comment13 = new Comment();
        comment13.setUserId(13L);
        comment13.setText(getRandomText());
        comment13.setCreatedDate(LocalDateTime.now());
        comment13.setPost(posts.get(6));
        comments.add(comment13);

        Comment comment14 = new Comment();
        comment14.setUserId(14L);
        comment14.setText(getRandomText());
        comment14.setCreatedDate(LocalDateTime.now());
        comment14.setPost(posts.get(7));
        comments.add(comment14);

        Comment comment15 = new Comment();
        comment15.setUserId(15L);
        comment15.setText(getRandomText());
        comment15.setCreatedDate(LocalDateTime.now());
        comment15.setPost(posts.get(7));
        comments.add(comment15);

        Comment comment16 = new Comment();
        comment16.setUserId(16L);
        comment16.setText(getRandomText());
        comment16.setCreatedDate(LocalDateTime.now());
        comment16.setPost(posts.get(8));
        comments.add(comment16);

        Comment comment17 = new Comment();
        comment17.setUserId(17L);
        comment17.setText(getRandomText());
        comment17.setCreatedDate(LocalDateTime.now());
        comment17.setPost(posts.get(8));
        comments.add(comment17);

        Comment comment18 = new Comment();
        comment18.setUserId(18L);
        comment18.setText(getRandomText());
        comment18.setCreatedDate(LocalDateTime.now());
        comment18.setPost(posts.get(8));
        comments.add(comment18);

        Comment comment19 = new Comment();
        comment19.setUserId(19L);
        comment19.setText(getRandomText());
        comment19.setCreatedDate(LocalDateTime.now());
        comment19.setPost(posts.get(9));
        comments.add(comment19);

        Comment comment20 = new Comment();
        comment20.setUserId(20L);
        comment20.setText(getRandomText());
        comment20.setCreatedDate(LocalDateTime.now());
        comment20.setPost(posts.get(10));
        comments.add(comment20);

        Comment comment21 = new Comment();
        comment21.setUserId(21L);
        comment21.setText(getRandomText());
        comment21.setCreatedDate(LocalDateTime.now());
        comment21.setPost(posts.get(10));
        comments.add(comment21);

        Comment comment22 = new Comment();
        comment22.setUserId(22L);
        comment22.setText(getRandomText());
        comment22.setCreatedDate(LocalDateTime.now());
        comment22.setPost(posts.get(10));
        comments.add(comment22);

        Comment comment23 = new Comment();
        comment23.setUserId(23L);
        comment23.setText(getRandomText());
        comment23.setCreatedDate(LocalDateTime.now());
        comment23.setPost(posts.get(10));
        comments.add(comment23);

        Comment comment24 = new Comment();
        comment24.setUserId(24L);
        comment24.setText(getRandomText());
        comment24.setCreatedDate(LocalDateTime.now());
        comment24.setPost(posts.get(11));
        comments.add(comment24);

        Comment comment25 = new Comment();
        comment25.setUserId(25L);
        comment25.setText(getRandomText());
        comment25.setCreatedDate(LocalDateTime.now());
        comment25.setPost(posts.get(11));
        comments.add(comment25);

        Comment comment26 = new Comment();
        comment26.setUserId(26L);
        comment26.setText(getRandomText());
        comment26.setCreatedDate(LocalDateTime.now());
        comment26.setPost(posts.get(11));
        comments.add(comment26);

        Comment comment27 = new Comment();
        comment27.setUserId(27L);
        comment27.setText(getRandomText());
        comment27.setCreatedDate(LocalDateTime.now());
        comment27.setPost(posts.get(11));
        comments.add(comment27);

        Comment comment28 = new Comment();
        comment28.setUserId(28L);
        comment28.setText(getRandomText());
        comment28.setCreatedDate(LocalDateTime.now());
        comment28.setPost(posts.get(11));
        comments.add(comment28);

        Comment comment29 = new Comment();
        comment29.setUserId(29L);
        comment29.setText(getRandomText());
        comment29.setCreatedDate(LocalDateTime.now());
        comment29.setPost(posts.get(11));
        comments.add(comment29);

        Comment comment30 = new Comment();
        comment30.setUserId(30L);
        comment30.setText(getRandomText());
        comment30.setCreatedDate(LocalDateTime.now());
        comment30.setPost(posts.get(11));
        comments.add(comment30);

        for (Comment comment : comments) {
            commentService.addComment(comment);
        }
    }

    private static String getRandomText() {
        String[] adjectives = {"Good job!", "Excellent!", "Terrific!", "Interesting!", "All Right!", "Exactly right!", "Exceptional!",
                "Fantastic!", "Exceptional!", "Wonderful!", "Outstanding!"};
        String[] additionalInfo = {"Thank you for writing this! I love it so much!!!",
                "Excuse me while I go tell my friends about this masterpiece you have created.",
                "THIS IS AN AMAZING FANFIC HOLY HECK I LOVE IT SO MUCH I REREAD IT IMMEDIATELY!!!",
                "Well done, my friend, well done.",
                "This story is written so well, the sadness/despair is well depicted; you have an excellent gift of carving and weaving emotions into your narrations.",
                "But that was then, right?",
                "So, game over. Blog  Stick a fork in them.",
                "That’s the word on the street, isn’t it?",
                "If — and this is the catch — you write a high-quality, genuinely-great blog comment worth noticing."};
        StringBuilder comment = new StringBuilder();
        String adj = adjectives[(int) (Math.random() * (adjectives.length))];
        String addInfo = additionalInfo[(int) (Math.random() * (additionalInfo.length))];
        return comment.append(adj).append("\n").append(addInfo).toString();
    }
}
