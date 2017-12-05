/*
 * Copyright 2014 Tagbangers, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wallride.domain;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NamedEntityGraphs({
		@NamedEntityGraph(name = Comment.SHALLOW_GRAPH_NAME,
				attributeNodes = {
						@NamedAttributeNode("author")}
		),
		@NamedEntityGraph(name = Comment.DEEP_GRAPH_NAME,
				attributeNodes = {
						@NamedAttributeNode("author")})
})
@Table(name = "comment")
@DynamicInsert
@DynamicUpdate
public class Comment extends DomainObject<Long> implements Comparable<Comment> {

	public static final String SHALLOW_GRAPH_NAME = "COMMENT_SHALLOW_GRAPH";
	public static final String DEEP_GRAPH_NAME = "COMMENT_DEEP_GRAPH";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Comment parent;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	private List<Comment> children;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	private User author;

	@Column(length = 200, nullable = false)
	private String authorName;

	@Column(nullable = false)
	private LocalDateTime date;

	@Lob
	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private boolean approved;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public Comment getParent() {
		return parent;
	}

	public void setParent(Comment parent) {
		this.parent = parent;
	}

	public List<Comment> getChildren() {
		return children;
	}

	public void setChildren(List<Comment> children) {
		this.children = children;
	}

	@Override
	public String print() {
		return this.getClass().getName() + " " + getId();
	}

	public int compareTo(Comment comment) {
		return new CompareToBuilder()
				.append(getDate(), comment.getDate())
				.append(getId(), comment.getId())
				.toComparison();
	}
}
