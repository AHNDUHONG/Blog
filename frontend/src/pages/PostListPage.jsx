import { useEffect, useState } from "react";
import { getPosts } from "../api/postApi";

const categories = [
    { value: "", label: "전체" },
    { value: "DAILY", label: "일상" },
    { value: "DEV_LOG", label: "개발일지" },
    { value: "PROJECT", label: "프로젝트" },
    { value: "NOTICE", label: "공지" },
];

function PostListPage() {
    const [posts, setPosts] = useState([]);
    const [pageInfo, setPageInfo] = useState({
        page: 0,
        size: 10,
        totalPages: 0,
        totalElements: 0,
        first: true,
        last: true,
    });

    const [page, setPage] = useState(0);
    const [keywordInput, setKeywordInput] = useState("");
    const [keyword, setKeyword] = useState("");
    const [category, setCategory] = useState("");
    const [loading, setLoading] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");

    const fetchPosts = async () => {
        try {
            setLoading(true);
            setErrorMessage("");

            const data = await getPosts({
                page,
                size: 10,
                keyword,
                category,
            });

            setPosts(data.content);
            setPageInfo({
                page: data.page,
                size: data.size,
                totalPages: data.totalPages,
                totalElements: data.totalElements,
                first: data.first,
                last: data.last,
            });
        } catch (error) {
            console.error(error);
            setErrorMessage("게시글 목록을 불러오지 못했습니다.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchPosts();
    }, [page, keyword, category]);

    const handleSearchSubmit = (event) => {
        event.preventDefault();
        setPage(0);
        setKeyword(keywordInput);
    };

    const handleCategoryChange = (event) => {
        setPage(0);
        setCategory(event.target.value);
    };

    const formatDateTime = (dateTime) => {
        if (!dateTime) return "";

        return new Date(dateTime).toLocaleString("ko-KR", {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
            hour: "2-digit",
            minute: "2-digit",
        });
    };

    const getCategoryLabel = (categoryValue) => {
        const foundCategory = categories.find(
            (item) => item.value === categoryValue
        );

        return foundCategory ? foundCategory.label : categoryValue;
    };

    return (
        <main style={{ maxWidth: "960px", margin: "0 auto", padding: "40px 20px" }}>
            <h1 style={{ fontSize: "32px", marginBottom: "24px" }}>개발 블로그</h1>

            <section
                style={{
                    display: "flex",
                    gap: "12px",
                    marginBottom: "24px",
                    alignItems: "center",
                }}
            >
                <select
                    value={category}
                    onChange={handleCategoryChange}
                    style={{ padding: "10px", border: "1px solid #ddd", borderRadius: "6px" }}
                >
                    {categories.map((item) => (
                        <option key={item.value} value={item.value}>
                            {item.label}
                        </option>
                    ))}
                </select>

                <form onSubmit={handleSearchSubmit} style={{ display: "flex", gap: "8px", flex: 1 }}>
                    <input
                        type="text"
                        value={keywordInput}
                        onChange={(event) => setKeywordInput(event.target.value)}
                        placeholder="검색어를 입력하세요"
                        style={{
                            flex: 1,
                            padding: "10px",
                            border: "1px solid #ddd",
                            borderRadius: "6px",
                        }}
                    />
                    <button
                        type="submit"
                        style={{
                            padding: "10px 16px",
                            border: "none",
                            borderRadius: "6px",
                            cursor: "pointer",
                        }}
                    >
                        검색
                    </button>
                </form>
            </section>

            {loading && <p>게시글을 불러오는 중입니다...</p>}

            {errorMessage && (
                <p style={{ color: "red", marginBottom: "16px" }}>{errorMessage}</p>
            )}

            {!loading && posts.length === 0 && (
                <p>게시글이 없습니다.</p>
            )}

            {!loading && posts.length > 0 && (
                <section>
                    {posts.map((post) => (
                        <article
                            key={post.id}
                            style={{
                                padding: "20px",
                                border: "1px solid #e5e5e5",
                                borderRadius: "10px",
                                marginBottom: "12px",
                            }}
                        >
                            <div style={{ marginBottom: "8px", color: "#666" }}>
                                {getCategoryLabel(post.category)}
                            </div>

                            <h2 style={{ fontSize: "22px", marginBottom: "12px" }}>
                                {post.title}
                            </h2>

                            <div style={{ display: "flex", gap: "12px", color: "#777", fontSize: "14px" }}>
                                <span>작성자: {post.authorNickname}</span>
                                <span>조회수: {post.viewCount}</span>
                                <span>{formatDateTime(post.createdAt)}</span>
                            </div>
                        </article>
                    ))}
                </section>
            )}

            <section
                style={{
                    display: "flex",
                    justifyContent: "center",
                    gap: "12px",
                    marginTop: "28px",
                }}
            >
                <button
                    onClick={() => setPage((prev) => prev - 1)}
                    disabled={pageInfo.first}
                    style={{ padding: "8px 14px", cursor: pageInfo.first ? "not-allowed" : "pointer" }}
                >
                    이전
                </button>

                <span style={{ padding: "8px 0" }}>
          {pageInfo.totalPages === 0 ? 0 : pageInfo.page + 1} / {pageInfo.totalPages}
        </span>

                <button
                    onClick={() => setPage((prev) => prev + 1)}
                    disabled={pageInfo.last}
                    style={{ padding: "8px 14px", cursor: pageInfo.last ? "not-allowed" : "pointer" }}
                >
                    다음
                </button>
            </section>
        </main>
    );
}

export default PostListPage;