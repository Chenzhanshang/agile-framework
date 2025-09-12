package com.agile.framework.specification;

/**
 * AND规约, 用于创建一个新规约，为两个规约的AND条件
 * @date: 2023/5/30 10:27
 * @author: chenzhanshang
 */
public class AndSpecification<T> extends AbstractSpecification<T> {

  private Specification<T> spec1;
  private Specification<T> spec2;

  /**
   * 基于两个规约创建一个新的AND规约
   * @param spec1 Specification one.
   * @param spec2 Specification two.
   */
  public AndSpecification(final Specification<T> spec1, final Specification<T> spec2) {
    this.spec1 = spec1;
    this.spec2 = spec2;
  }

  /**
   * 新规约的满足条件
   */
  @Override
  public boolean isSatisfiedBy(final T t) {
    return spec1.isSatisfiedBy(t) && spec2.isSatisfiedBy(t);
  }
}
