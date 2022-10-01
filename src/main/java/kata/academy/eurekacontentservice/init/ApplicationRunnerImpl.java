package kata.academy.eurekacontentservice.init;

import kata.academy.eurekacontentservice.model.entity.Comment;
import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.service.CommentService;
import kata.academy.eurekacontentservice.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
@Profile("dev")
public class ApplicationRunnerImpl implements ApplicationRunner {
    private final PostService postService;
    private final CommentService commentService;

    @Override
    public void run(ApplicationArguments args) {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post(1L, 1L, "The Competition",
                "A “Share” vs. “Like” post is a great way for people to engage with your brand by offering their " +
                        "opinion (which Facebook users love to do!). Basically, this post offers two different options.",
                LocalDateTime.now(),List.of(new String[]{"like", "post"})));

        posts.add(new Post(2L, 1L, "The Poll",
                "The poll is a simple but effective way to get your Fans to involve themselves in your brand. " +
                        "This post includes 2-4 options, and then asks Fans for their favorite. It’s also a great way. ",
                LocalDateTime.now(),List.of(new String[]{"fans", "poll"})));

        posts.add(new Post(3L, 1L, "Ask for it",
                "Sometimes the simplest, most straightforward idea is the best. Just like with Twitter, in which " +
                        "asking for a retweet increases the chance of it being done by 23 times, being clear about.",
                LocalDateTime.now(),List.of(new String[]{"ask", "reward"})));

        posts.add(new Post(4L, 2L, "Give a sneak peek",
                "Giving a sneak peek is one of the more complex Facebook product posts, but has a more lasting " +
                        "draw. Simply find something your users haven’t seen yet and offer a preview",
                LocalDateTime.now(),List.of(new String[]{"sneak", "peak"})));

        posts.add(new Post(5L, 2L, "Offer Advice",
                "A look at 682 Facebook posts found that posts containing advice or teaching something new were " +
                        "shared 522% more than posts that weren’t advice related. Whether it’s a recipe, a statistic.",
                LocalDateTime.now(),List.of(new String[]{"offer", "advice"})));

        posts.add(new Post(6L, 3L, "Geo-Target your Audience",
                "By intentionally limiting your target audience you create exclusivity, drawing in those ‘select’ " +
                        "Fans who feel attached to your subject. Do this by providing a specific picture or hashtag.",
                LocalDateTime.now(),List.of(new String[]{"geo-target", "facebook"})));

        posts.add(new Post(7L, 3L, "Use Universal Appeal",
                "Whether it’s an inspiring or funny quote, an easily-agreed-upon opinion, or an interesting fact.",
                LocalDateTime.now(),List.of(new String[]{"appeal", "like"})));

        posts.add(new Post(8L, 3L, "Showcase your Fans",
                "Facebook is a conversation and showcasing your Fan’s talent is a great way to create an exclusive " +
                        "community. Your Fans will respond more enthusiastically and more often if they feel you’re.",
                LocalDateTime.now(),List.of(new String[]{"fans", "facebook"})));

        posts.add(new Post(9L, 4L, "Go Adorable",
                "A quick and easy way to create some Facebook attention is to use a cute or appealing image. In many " +
                        "ways this strategy is similar to Coca-Cola’s ‘universal appeal’ technique. Simply find a picture.",
                LocalDateTime.now(),List.of(new String[]{"adorable", "coca-cola"})));

        posts.add(new Post(10L, 4L, "Use Real People",
                "Using real people is one of the best ways to halt a Facebook user mid-scroll. People expect to see " +
                        "celebrities and historical figures so a genuine face, with a story behind it, can be.",
                LocalDateTime.now(),List.of(new String[]{"people", "facebook"})));

        posts.add(new Post(11L, 5L, "Use Memes",
                "Depending on your social media persona and tone, using a meme or humorous Ecard can be a great way " +
                        "to engage with your Fans. Used wisely they work much like Coca-Cola’s inspirational quote and HP.",
                LocalDateTime.now(),List.of(new String[]{"memes", "like"})));

        posts.add(new Post(12L, 5L, "Check your Links",
                "By prioritizing the editorial process and double-checking your work, you can limit those little human " +
                        "errors that can make all the difference. But no matter how hard we try, the intricacies of social.",
                LocalDateTime.now(),List.of(new String[]{"links", "media"})));
        for (int i = 0; i < posts.size(); i++) {
            postService.addPost(posts.get(i));
        }

        long commentsCounter = 1L;
        for (long i = 1; i <= 30; i++) {
            for (int j = 0; j < posts.size(); j++) {
                int commentsAmount = (int) (Math.random() * 2) + 1;
                for (int k = 1; k <= commentsAmount; k++) {
                    Comment comment = new Comment(commentsCounter, i, getRandomComment(), LocalDateTime.now(), posts.get(j));
                    commentService.addComment(comment);
                    commentsCounter++;
                }
            }
        }
    }
    private static String getRandomComment() {
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
