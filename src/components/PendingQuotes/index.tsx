"use client";

import SinglePendingQuote from "./SinglePendingQuote";
import { Suspense, useEffect, useState } from "react";
import axios from "axios";
import { useSession } from "next-auth/react";
import Search from "@/components/Search";
import Pagination from "@/components/Pagination";
import PageElements from "@/components/Dropdown/PageElements";
import SortOrder from "@/components/Dropdown/SortOrder";

export default function PendingQuotes({ searchParams }: {
  searchParams?: {
    query?: string;
    page?: string;
    size?: string;
    sort?: string;
  };
}) {
  const query = searchParams?.query || "";
  const currentPage = Number(searchParams?.page) || 1;
  const size = searchParams?.size || 3;
  const sort = searchParams?.sort || "changeDate";

  const { data: session } = useSession();
  const [quotes, setQuotes] = useState({
    totalPages: 0,
    content: []
  });
  const API_URL = process.env.NEXT_PUBLIC_API_URL;

  useEffect(() => {
    axios.get(API_URL + `/quotes/pending?keyword=${query}&page=${currentPage - 1}&size=${size}&sort=${sort},desc`,
      {
        headers: {
          Authorization: `Bearer ${session.accessToken}`
        }
      }
    )
      .then(response => {
        setQuotes(response.data);
      })
      .catch(error => {
        console.error(error);
      });
  });

  return (
    <section className="dark:bg-bg-color-dark bg-gray-light relative z-10 py-16 md:py-20 lg:py-28">
      <div className="container">
        <div className="mt-10">
          <Search placeholder="Поиск цитат в ожидании опубликования по ключевому слову" />
        </div>
        <Suspense key={query + currentPage}>
          <div className="grid grid-cols-1 gap-x-8 gap-y-10 md:grid-cols-2 lg:grid-cols-3">
            {quotes.content.map((q) => (
              <SinglePendingQuote key={q.id} quote={q} />
            ))}
          </div>
        </Suspense>
        <div className="mt-10 flex w-full justify-center">
          <Pagination totalPages={quotes.totalPages} />
        </div>
        <div className="flex w-full justify-between">
          <span>Цитат на странице: <PageElements /></span>
          <SortOrder />
        </div>
      </div>
    </section>
  );
};
