package org.wallride.web.controller.guest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wallride.domain.Language;
import org.wallride.domain.Tag;
import org.wallride.service.PostService;
import org.wallride.service.TagService;
import org.wallride.web.support.HttpNotFoundException;
import org.wallride.web.support.Pagination;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/tag")
public class TagController {

	@Autowired
	private TagService tagService;

	@Autowired
	private PostService postService;

	@RequestMapping
	public String index() {

		return "tag/index";
	}

	@RequestMapping(value = "/data")
	public String guestTags(
			@PageableDefault Pageable pageable,
			Model model,
			HttpServletRequest servletRequest) {
		Page<Tag> tags = tagService.getTags(new TagSearchRequest(), pageable);
		model.addAttribute("tags", tags);
		model.addAttribute("pageable", pageable);
		model.addAttribute("pagination", new Pagination<>(tags, servletRequest));
		return "tag/tags";
	}

	@RequestMapping("/{name}")
	public String post(@PathVariable String name, Model model) {

		model.addAttribute("tagName", name);
		return "tag/post";
	}

	@RequestMapping("/{name}/data")
	public String post(
			@PathVariable String name,
			@PageableDefault Pageable pageable,
			Language language,
			Model model,
			HttpServletRequest servletRequest) {
		Tag tag = tagService.getTagByName(name, language.getLanguage());
		if (tag == null) {
			tag = tagService.getTagByName(name, language.getBlog().getDefaultLanguage());
		}
		if (tag == null) {
			throw new HttpNotFoundException();
		}
		Page<Post> posts = postService.getPostsByTag(tag, pageable);
		model.addAttribute("tag", tag);
		model.addAttribute("posts", posts);
		model.addAttribute("pageable", pageable);
		model.addAttribute("pagination", new Pagination<>(posts, servletRequest));
		return "tag/tag-of-posts";
	}
}
