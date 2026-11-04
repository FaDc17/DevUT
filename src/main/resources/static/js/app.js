document.addEventListener('DOMContentLoaded', () => {
  const menu = document.getElementById('menuButton');
  const nav = document.getElementById('navLinks');
  menu?.addEventListener('click', () => nav?.classList.toggle('nav-open'));

  const togglePassword = document.getElementById('togglePassword');
  togglePassword?.addEventListener('click', () => {
    const input = document.getElementById('password'); input.type = input.type === 'password' ? 'text' : 'password';
  });
  const search = document.getElementById('eventSearch');
  const category = document.getElementById('categoryFilter');
  const filterEvents = () => {
    const cards = [...document.querySelectorAll('#eventList .event-card')]; let visible = 0;
    cards.forEach(card => {
      const matchesSearch = card.dataset.title.toLowerCase().includes((search?.value || '').toLowerCase());
      const matchesCategory = !category || category.value === 'Todas las categorías' || card.dataset.category === category.value;
      card.hidden = !(matchesSearch && matchesCategory); if (!card.hidden) visible++;
    });
    const count = document.getElementById('resultCount'); if (count) count.firstChild.textContent = visible + ' eventos';
  };
  search?.addEventListener('input', filterEvents); category?.addEventListener('change', filterEvents);

  const commentInput = document.getElementById('commentInput');
  commentInput?.addEventListener('input', () => document.getElementById('commentButton').disabled = !commentInput.value.trim());
  document.getElementById('commentForm')?.addEventListener('submit', event => {
    event.preventDefault(); if (!commentInput.value.trim()) return;
    const article = document.createElement('article'); article.className = 'comment-item';
    const displayName = document.body.dataset.displayName || 'Usuario';
    article.innerHTML = '<span class="avatar avatar-md" aria-hidden="true"></span><div><strong></strong><p></p><span>Ahora</span></div>';
    article.querySelector('strong').textContent = displayName;
    article.querySelector('p').textContent = commentInput.value.trim(); document.getElementById('commentList').prepend(article); commentInput.value=''; document.getElementById('commentButton').disabled=true;
  });

  const publishModal = document.getElementById('publishModal');
  const closePublishModal = () => {
    if (publishModal) publishModal.hidden = true;
  };
  document.getElementById('openPublish')?.addEventListener('click', () => {
    if (publishModal) publishModal.hidden = false;
  });
  document.getElementById('closePublish')?.addEventListener('click', closePublishModal);
  publishModal?.addEventListener('click', (event) => {
    if (event.target === publishModal) closePublishModal();
  });
  const publishText = document.getElementById('publishText'); publishText?.addEventListener('input', () => document.getElementById('publishButton').disabled=!publishText.value.trim());
  document.getElementById('publishButton')?.addEventListener('click', () => {
    if (!publishText.value.trim()) return; const post=document.createElement('article'); post.className='post-card';
    const displayName = document.body.dataset.displayName || 'Usuario';
    post.innerHTML='<div class="post-author"><span class="avatar avatar-md" aria-hidden="true"></span><div><strong></strong><span>Ahora</span></div></div><p></p>';
    post.querySelector('strong').textContent=displayName; post.querySelector('p').textContent=publishText.value.trim();
    document.getElementById('newPosts').prepend(post); publishText.value=''; publishModal.hidden=true;
  });

  const profileEditModal = document.getElementById('profileEditModal');
  const closeProfileEditModal = () => {
    if (profileEditModal) profileEditModal.hidden = true;
  };
  document.getElementById('openProfileEdit')?.addEventListener('click', () => {
    if (profileEditModal) profileEditModal.hidden = false;
  });
  document.getElementById('closeProfileEdit')?.addEventListener('click', closeProfileEditModal);
  document.getElementById('cancelProfileEdit')?.addEventListener('click', closeProfileEditModal);
  profileEditModal?.addEventListener('click', event => {
    if (event.target === profileEditModal) closeProfileEditModal();
  });

  document.querySelectorAll('.admin-tab').forEach(button => button.addEventListener('click', () => {
    document.querySelectorAll('.admin-panel').forEach(panel => panel.hidden = panel.id !== button.dataset.panel);
    document.querySelectorAll('.admin-menu .admin-tab').forEach(tab => tab.classList.toggle('active', tab.dataset.panel === button.dataset.panel));
  }));

  const params = new URLSearchParams(location.search);
  if (params.get('tab') === 'list') { const list = document.querySelector('[data-panel="list"]'); list?.click(); }
});
