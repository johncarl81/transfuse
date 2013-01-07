---
layout: default
title: Transfuse
documentationExpanded: false
postsExpanded: true
---


### Transfuse Technical Blog

<ul>
	{% for post in site.posts %}
	<li>
		<h4><a href="{{ post.url }}" class="title" title="{{ post.title }}">{{ post.title }}</a></h4>
		<span class="date">
			<span class="month"><abbr>{{ post.date | date: '%b' }}</abbr></span>
			<span class="day">{{ post.date | date: '%d' }}</span>
			<span class="year">{{ post.date | date: '%Y' }}</span>
		</span>
		<br/>
		<span class="excerpt">{{ post.excerpt }}  <a href="{{ post.url }}">Read more...</a></span>
		<hr/>
	</li>
	{% endfor %}
</ul>
